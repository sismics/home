'use strict';

var express = require('express');
var app = express();

/**
 * Application status.
 */
app.get('/status', function(req, res) {
  var pjson = require('./package.json');

  res.json({
    "name": pjson.name,
    "version": pjson.version
  });
});

/**
 * Booting server.
 */
var server = app.listen(3000, function() {
  console.log('Listening on port %d', server.address().port);
});

/**
 * Error handling.
 */
app.use(function(err, req, res){
  console.error(err.stack);
  res.send(500, 'Internal server error');
});