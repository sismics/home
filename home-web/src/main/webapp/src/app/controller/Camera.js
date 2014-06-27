'use strict';

/**
 * Camera controller.
 */
angular.module('home').controller('Camera', function($scope, Restangular) {
  Restangular.one('camera', 'main-camera').get().then(function(data) {
    $scope.camera = data;
  });
});