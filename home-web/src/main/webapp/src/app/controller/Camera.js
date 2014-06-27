'use strict';

/**
 * Camera controller.
 */
angular.module('home').controller('Camera', function($scope, Restangular) {
  $scope.imageUrl = null;

  Restangular.one('camera', 'main-camera').get().then(function(data) {
    $scope.camera = data;
    $scope.buildImageUrl();
  });

  $scope.buildImageUrl = function() {
    $scope.imageUrl = '../api/camera/' + $scope.camera.id + '/picture?' + new Date().getTime();
  };
});