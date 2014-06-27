'use strict';

/**
 * Navigation controller.
 */
angular.module('home').controller('Navigation', function($rootScope, $http, $scope, User, $state) {
  // Returns true if at least an asynchronous request is in progress
  $scope.isLoading = function() {
    return $http.pendingRequests.length > 0;
  };

  // Watch user info for admin status
  $scope.$watch('userInfo', function(userInfo) {
    $scope.isAdmin = userInfo && userInfo.base_functions && userInfo.base_functions.indexOf('ADMIN') != -1;
  });
  
  // Load user data
  User.userInfo().then(function(data) {
    $rootScope.userInfo = data;
  });
  
  // User logout
  $scope.logout = function($event) {
    User.logout().then(function() {
      User.userInfo(true).then(function(data) {
        $rootScope.userInfo = data;
      });
      $state.transitionTo('login');
    });
    $event.preventDefault();
  };
});