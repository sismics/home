'use strict';

var tessel = require('tessel');
var http = require('http');
var climatelib = require('climate-si7020');

/**
 * Climate module.
 */
var climate = climatelib.use(tessel.port['A']);

climate.on('ready', function () {
  console.log('Connected to si7020!');

  // Loop forever
  setImmediate(function loop() {
    climate.readTemperature('c', function (err, temp) {
      climate.readHumidity(function (err, humid) {
        updateSensor('main-temp', temp);
        updateSensor('main-humidity', humid);
        setTimeout(loop, 6000);
      });
    });
  });
});

/**
 * CurrentCost EnviR interface.
 */
var port = tessel.port['B'];
var uart = new port.UART({
  baudrate: 57600
});

var buffer = '';
uart.on('data', function (data) {
  buffer += data.toString();
  if (buffer.trim().endsWith('</msg>')) {
    var match = /<watts>(\d*?)<\/watts>/gm.exec(buffer.toString());
    if (match) {
      updateSensor('main-elec', parseInt(match[1]));
    }
	buffer = '';
  }
});

/**
 * Update a sensor.
 *
 * @param id Sensor ID
 * @param value Sensor value
 */
var updateSensor = function(id, value) {
  dataQueue.push({ id: id, value: value, date: new Date().getTime() });
};

/**
 * Main update loop.
 */
var dataQueue = [];
setImmediate(function mainLoop() {
  var options = {
    hostname: 'myhome.bgamard.org',
    port: 80,
    path: '/api/sensor/sample',
    method: 'PUT',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  };

  tessel.led[2].on();
  var req = http.request(options, function(res) {
    res.setEncoding('utf8');
    res.on('data', function () {
	  tessel.led[2].off();
      console.log('Sensors updated!');
    });
  });

  req.on('error', function(e) {
    console.log('problem with request: ' + e.message);
  });

  for (var i = 0; i < dataQueue.length; i++) {
    var data = dataQueue[i];
    req.write('id=' + data.id + '&date=' + data.date + '&value=' + data.value + '&');
    console.log('Updating sensor [' + data.id + ', ' + data.date + '] with value: ' + data.value);
  }

  req.end();

  dataQueue = [];
  setTimeout(mainLoop, 6000);
});

console.log('Tessel started!');