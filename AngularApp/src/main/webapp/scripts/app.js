'use strict';

/**
 * @ngdoc overview
 * @name appWebApp
 * @description
 * # appWebApp
 *
 * Main module of the application.
 */
angular
  .module('webClientApp', ['ngRoute', 'ui.bootstrap','dialogs', 'pascalprecht.translate', 'ngStorage']) 
 .config(function ($routeProvider, $translateProvider, $httpProvider) {
    $routeProvider
  	.when('/', {
        templateUrl: 'login.html',
        controller: 'LoginCtrl'
      })
      .when('/recorridos', {
        templateUrl: 'views/recorridos.html',
        controller: 'FactoresretencionCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
    
		$translateProvider.useStaticFilesLoader({
        prefix: 'res/locale-',
        suffix: '.json'
      });

      $translateProvider.preferredLanguage("es_CO");
      
      $httpProvider.interceptors.push(['$q', '$location', '$localStorage', function($q, $location, $localStorage) {
          return {
              'request': function (config) {
                  config.headers = config.headers || {};
                  
                  //if ($localStorage.token) {
            	  if (localStorage["token"] && localStorage["username"]) {
                      //config.headers.Authorization = $localStorage.token;
                      config.headers.Authorization = localStorage["username"] +"@-"+localStorage["token"];
                  }
                  return config;
              },
              'responseError': function(response) {
                  if(response.status === 401 || response.status === 403 || response.status === 409) {
                      //$location.path('/');
                      window.location = "login.html#/";
                  }
                  return $q.reject(response);
              }
          };
      }]);

 
});