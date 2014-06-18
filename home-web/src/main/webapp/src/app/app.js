'use strict';

/**
 * Sismics Home application.
 */
angular.module('home',
        // Dependencies
        ['ui.router', 'restangular', 'mgcrea.ngStrap'])

    /**
     * Configuring modules.
     */
    .config(function ($stateProvider, RestangularProvider, $httpProvider, $uiViewScrollProvider) {
      // Configuring UI Router
      $uiViewScrollProvider.useAnchorScroll();
      $stateProvider
          .state('login', {
            url: '/login',
            views: {
              'page': {
                templateUrl: 'partial/login.html',
                controller: 'Login'
              }
            }
          })
          .state('main', {
            url: '',
            views: {
              'page': {
                templateUrl: 'partial/main.html'
              }
            }
          });

      // Configuring Restangular
      RestangularProvider.setBaseUrl('../api');

      // Configuring $http to act like jQuery.ajax
      $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
      $httpProvider.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
      $httpProvider.defaults.transformRequest = [function (data) {
        var param = function (obj) {
          var query = '';
          var name, value, fullSubName, subName, subValue, innerObj, i;

          for (name in obj) {
            value = obj[name];

            if (value instanceof Array) {
              for (i = 0; i < value.length; ++i) {
                subValue = value[i];
                fullSubName = name;
                innerObj = {};
                innerObj[fullSubName] = subValue;
                query += param(innerObj) + '&';
              }
            } else if (value instanceof Object) {
              for (subName in value) {
                subValue = value[subName];
                fullSubName = name + '[' + subName + ']';
                innerObj = {};
                innerObj[fullSubName] = subValue;
                query += param(innerObj) + '&';
              }
            }
            else if (value !== undefined && value !== null) {
              query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
            }
          }

          return query.length ? query.substr(0, query.length - 1) : query;
        };

        return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
      }];
    })

    /**
     * Application initialization.
     */
    .run(function ($rootScope, $state, $stateParams) {
      $rootScope.$state = $state;
      $rootScope.$stateParams = $stateParams;
    });