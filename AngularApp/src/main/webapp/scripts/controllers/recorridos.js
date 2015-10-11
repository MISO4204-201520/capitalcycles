'use strict';

/**
 * @ngdoc function
 * @name appWebApp.controller:FactoresretencionCtrl
 * @description
 * # FactoresretencionCtrl
 * Controller of the appWebApp
 */
angular.module('webClientApp')
  .controller('FactoresretencionCtrl', function ($scope, $rootScope, $http,  $dialogs, $location) {
	  
      	$scope.factores = [];
	
	    console.log("Search all factores");
	    $http.get("http://"+window.location.host+"/negocio-apiRest/rest/FactoresRetencion")
	    
	    .success(function(data){
	    $rootScope.$broadcast('dialogs.wait.complete');
	    $scope.factores=data; 
	    
	    }).error(function(data){	  	  
	        console.log("Doesn't work");
	        $rootScope.$broadcast('dialogs.wait.complete');
	    });
	    
	    $scope.verDetalle=function(idFactorRetencion){
	    	console.log("redireccion ver detalle");
	    	$location.path('/factorretencion-detail/' + idFactorRetencion );
        }
	    
	    $scope.actualizar=function(idFactorRetencion){
	    	console.log("redireccion Actualizar");
	    	$location.path('/factorretencion-edit/' + idFactorRetencion );
        }
 
    
  });
