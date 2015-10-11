'use strict';

/**
 * @ngdoc function
 * @name appWebApp.controller:FactorretencionAddCtrl
 * @description
 * # FactorretencionAddCtrl
 * Controller of the appWebApp
 */
angular.module('webClientApp')
  .controller('MainCtrl', function ($scope, $rootScope, $http,  $dialogs) {
	  
	  
	  $scope.logout=function(){
		  localStorage["token"] = null;
          window.location = "login.html#/";
	  }
  });