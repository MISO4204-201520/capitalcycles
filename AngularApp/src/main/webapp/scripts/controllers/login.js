'use strict';

/**
 * @ngdoc function
 * @name appWebApp.controller:FactorretencionAddCtrl
 * @description
 * # LoginCtrl
 * Controller of the appWebApp
 */
angular.module('webClientApp')
  .controller('LoginCtrl', ['$scope', '$rootScope', '$http',  '$dialogs', '$location', '$localStorage', function ($scope, $rootScope, $http,  $dialogs, $location, $localStorage) {
  	
	    //Declaración del objeto Usuario
	 	function Usuario() {}
	    $scope.usuario = new Usuario();
 				
		$scope.login=function(){
            console.log("save factor");
            console.log("factor:"+angular.toJson( $scope.usuario));
            $http.post("http://"+window.location.host+"/sf-cc-gestion-usuario/rest/seguridadService/esValidoUsuario", angular.toJson( $scope.usuario))
            
            .success(function(data){
                console.log(data);
                //$localStorage.token = data.token;
                if(data.codigo==0){                	
	                localStorage["token"] = data.token;
	                localStorage["username"] = data.username;
	                window.location = "index.html#/recorridos";
                }else{
                	console.log("Doesn't work");
 				    $dialogs.notify('Error!','No es posible autenticar');
                }
            }).error(function(data){
                console.log("Doesn't work");
            });
        };

  }]);