$( document ).ready(function() {
	posicion();
	setTimeout(dibujar, 1000);
});

var contadorMarkers = 0;
var puntos = new Array();
var drawingManager = null;
var latPosicion = null;
var lngPosicion = null;

function posicion(){
	if (navigator.geolocation) {
        checkGeolocationByHTML5();
    } else {
        checkGeolocationByLoaderAPI(); // HTML5 not supported! Fall back to Loader API.
    }

    function checkGeolocationByHTML5() {
        navigator.geolocation.getCurrentPosition(function(position) {
            setMapCenter(position.coords.latitude, position.coords.longitude);
        }, function() {
            checkGeolocationByLoaderAPI(); // Error! Fall back to Loader API.
        });
    }

    function checkGeolocationByLoaderAPI() {
        if (google.loader.ClientLocation) {
            setMapCenter(google.loader.ClientLocation.latitude, google.loader.ClientLocation.longitude);
        } else {
            // Unsupported! Show error/warning?
        }
    }
}

function setMapCenter(latitude, longitude) {
	latPosicion = latitude;
	lngPosicion = longitude;
	PF('w_gmap').getMap().setCenter(new google.maps.LatLng(latitude, longitude));
	var imagen = {
		    url: "../resources/imagenes/logoCycle.png",
		    scaledSize: new google.maps.Size(30, 30)
		};
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(latitude, longitude),
        map: PF('w_gmap').getMap(),
        icon: imagen
      });
	var informacion = new google.maps.InfoWindow({
        content: "Esta aquí!!"
    });
	google.maps.event.addListener(marker, 'click', function () {
		informacion.open(PF('w_gmap').getMap(),marker);
    });
	informacion.open(PF('w_gmap').getMap(),marker);
}   

function dibujar(){
 	drawingManager = new google.maps.drawing.DrawingManager({
		drawingMode: google.maps.drawing.OverlayType.MARKER,
		drawingControl: true,
		drawingControlOptions: {
			position: google.maps.ControlPosition.TOP_CENTER,
			drawingModes: [
			               google.maps.drawing.OverlayType.MARKER
			               ]
		},
		markerOptions: {
			clickable: true,
			editable: true,
			draggable: true
		}
	});
	drawingManager.setMap(PF('w_gmap').getMap());
	google.maps.event.addListener(drawingManager, 'markercomplete', function(newMarker) {
		if (contadorMarkers==0){
			newMarker.setLabel("I");	
		}else if (contadorMarkers==1){
			newMarker.setLabel("F");	
		}
		puntos.push(newMarker);
		capturarCoordenadas();
		drawingManager.setMap(PF('w_gmap').getMap());
		google.maps.event.addListener(newMarker,'dragend',function(evento) {
			if (evento && evento.latLng){
				capturarCoordenadas();
			}
	    });
		contadorMarkers++;
		if (contadorMarkers==2){
			drawingManager.setDrawingMode(null);
			drawingManager.drawingControl = false;
		}
	});
}

function capturarCoordenadas(){
	if (puntos){
		if (puntos.length <= 2 && puntos[0]){
			latInicio=document.getElementById('formRecorridos:latInicio');
			lngInicio=document.getElementById('formRecorridos:lngInicio');
			if (latInicio){
				document.getElementById('formRecorridos:latInicio').value=puntos[0].getPosition().lat();
			}
			if (lngInicio){
				document.getElementById('formRecorridos:lngInicio').value=puntos[0].getPosition().lng();
			}
		}
		
		if (puntos.length <= 2 && puntos[1]){
			latFin=document.getElementById('formRecorridos:latFin');
			lngFin=document.getElementById('formRecorridos:lngFin');
			if (latFin){
				document.getElementById('formRecorridos:latFin').value=puntos[1].getPosition().lat();
			}
			if (lngFin){
				document.getElementById('formRecorridos:lngFin').value=puntos[1].getPosition().lng();
			}
		}
	}
}

function limpiarMapa(){
	if (puntos && puntos.length<=2){
		if (puntos.length <= 2 && puntos[0]){
			latInicio=document.getElementById('formRecorridos:latInicio');
			lngInicio=document.getElementById('formRecorridos:lngInicio');
			if (latInicio){
				document.getElementById('formRecorridos:latInicio').value="";
			}
			if (lngInicio){
				document.getElementById('formRecorridos:lngInicio').value="";
			}
		}
		
		if (puntos.length <= 2 && puntos[1]){
			latFin=document.getElementById('formRecorridos:latFin');
			lngFin=document.getElementById('formRecorridos:lngFin');
			if (latFin){
				document.getElementById('formRecorridos:latFin').value="";
			}
			if (lngFin){
				document.getElementById('formRecorridos:lngFin').value="";
			}
		}
		
		if (puntos){
			for (var i=0;i<puntos.length;i++){
				puntos[i].setMap(null);
			}	
		}
		
		puntos = new Array();
		contadorMarkers = 0;
	}
	
	if (drawingManager){
		drawingManager.drawingControl = true;
		drawingManager.setMap(PF('w_gmap').getMap());	
		drawingManager.setDrawingMode(google.maps.drawing.OverlayType.MARKER);
	}
	repintar();
}

function repintar(){
	if (latPosicion && lngPosicion){
		setMapCenter(latPosicion,lngPosicion);
	}
}