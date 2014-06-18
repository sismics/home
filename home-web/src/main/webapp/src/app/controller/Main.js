'use strict';

/**
 * Main controller.
 */
angular.module('home').controller('Main', function($scope, Restangular) {
  $scope.data = [];

  Restangular.one('elec_meter', 'main').get().then(function(data) {
    _(data.samples).each(function(sample) {
      $scope.data.push([sample.date, sample.value]);
    });
  });

  $scope.chartConfig = {
    options: {
      chart: {
        type: 'spline'
      },
      plotOptions: {
        spline: {
          lineWidth: 3
        }
      },
      xAxis: {
        type: 'datetime'
      },
      yAxis: {
        title: {
          text: 'Watts'
        }
      },
      title: null
    },
    series: [{
      data: $scope.data,
      name: 'Energy output'
    }],
    loading: false
  };
});