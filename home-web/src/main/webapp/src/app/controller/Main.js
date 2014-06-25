'use strict';

/**
 * Main controller.
 */
angular.module('home').controller('Main', function($scope, $interval, $http, Restangular) {
  $scope.data0 = [];
  $scope.data0Type = 'MINUTE';
  $scope.data1 = [];

  // Refresh sensors
  $scope.refreshSensors = function() {
    Restangular.one('sensor', 'main-elec').get({ sampleType: $scope.data0Type }).then(function(data) {
      $scope.data0.length = 0;
      _(data.samples).each(function(sample) {
        $scope.data0.push([sample.date, sample.value]);
      });
    });

    Restangular.one('sensor', 'internal-temp').get({ sampleType: 'MINUTE' }).then(function(data) {
      $scope.data1.length = 0;
      _(data.samples).each(function(sample) {
        $scope.data1.push([sample.date, sample.value]);
      });
    });
  };

  // Grab sensor data regulary
  $scope.refreshSensors();
  var interval = $interval(function() {
    $scope.refreshSensors();
  }, 6000);

  // Destroy interval with scope
  $scope.$on('destroy', function() {
    $interval.cancel(interval);
  });

  // Load weather
  // TODO Get the real city
  // TODO Grab weather forecast: http://api.openweathermap.org/data/2.5/forecast?q=Lyon,fr
  $http.jsonp('http://api.openweathermap.org/data/2.5/weather?q=Lyon,fr&callback=JSON_CALLBACK')
      .success(function(data) {
        $scope.weather = data;
      });

  // Configure Highcharts globally
  Highcharts.setOptions({
    global: {
      useUTC: false
    }
  });

  // Chart configuration 0
  $scope.chartConfig0 = {
    options: {
      colors: ['#e74c3c'],
      chart: {
        type: 'line',
        height: 296
      },
      plotOptions: {
        line: {
          lineWidth: 3,
          marker: {
            enabled: false
          }
        }
      },
      xAxis: {
        type: 'datetime'
      },
      yAxis: {
        title: {
          text: 'Watts'
        },
        min: 0
      },
      title: null
    },
    series: [{
      data: $scope.data0,
      name: 'Energy output'
    }],
    loading: false
  };

  // Chart configuration 1
  $scope.chartConfig1 = {
    options: {
      colors: ['#f39c12'],
      chart: {
        type: 'line',
        height: 362
      },
      plotOptions: {
        line: {
          lineWidth: 3,
          marker: {
            enabled: false
          }
        }
      },
      xAxis: {
        type: 'datetime'
      },
      yAxis: {
        title: {
          text: '°C'
        },
        min: 0
      },
      title: null
    },
    series: [{
      data: $scope.data1,
      name: 'Temperature'
    }],
    loading: false
  };

  // Corresponding OpenWeatherMap icons with Weather icons
  $scope.weatherIcon = function(owpIcon) {
    switch (owpIcon) {
      case '01d': return 'wi-day-sunny';
      case '02d': return 'wi-day-cloudy';
      case '03d': return 'wi-cloud';
      case '04d': return 'wi-cloudy';
      case '09d': return 'wi-rain';
      case '10d': return 'wi-day-rain';
      case '11d': return 'wi-thunderstorm';
      case '13d': return 'wi-day-snow';
      case '50d': return 'wi-day-fog';
      case '01n': return 'wi-night-clear';
      case '02n': return 'wi-day-cloudy';
      case '03n': return 'wi-cloud';
      case '04n': return 'wi-cloudy';
      case '09n': return 'wi-rain';
      case '10n': return 'wi-night-rain';
      case '11n': return 'wi-thunderstorm';
      case '13n': return 'wi-night-snow';
      case '50n': return 'wi-night-fog';
      default: return '';
    }
  }
});