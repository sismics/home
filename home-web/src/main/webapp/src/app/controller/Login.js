'use strict';

/**
 * Login controller.
 */
angular.module('home').controller('Login', function($rootScope, $scope, $state, User, $modal) {
  $scope.login = function() {
    User.login($scope.user).then(function() {
      User.userInfo(true).then(function(data) {
        $rootScope.userInfo = data;
      });

      $state.transitionTo('main');
    }, function() {

      $modal({title: 'Login failed', content: 'Username or password invalid', show: true});
    });
  };
});