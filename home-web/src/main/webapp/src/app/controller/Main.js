'use strict';

/**
 * Main controller.
 */
angular.module('home').controller('Main', function(User, $state) {
  User.userInfo().then(function(data) {
    if (data.anonymous) {
      $state.transitionTo('login');
    } else {
      $state.transitionTo('dashboard');
    }
  });
});