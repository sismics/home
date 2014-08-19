'use strict';

var tessel = require('tessel');
var http = require('http');
var climatelib = require('climate-si7020');

/**
 * Climate module.
 */
var climate = climatelib.use(tessel.port['B']);

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
var port = tessel.port['A'];
var uart = new port.UART({
  baudrate: 57600
});

uart.on('data', function (data) {
  var rg = /<watts>(\d*?)<\/watts>/gm;
  var match = rg.exec(data.toString());
  if (match) {
    updateSensor('main-elec', parseInt(match[1]));
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
    hostname: '192.168.1.10',
    port: 9999,
    path: '/home-web/api/sensor/sample',
    method: 'PUT',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  };

  var req = http.request(options, function(res) {
    res.setEncoding('utf8');
    res.on('data', function () {
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