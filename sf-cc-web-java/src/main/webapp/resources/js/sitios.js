$( document ).ready(function() {
	posicion();
	setTimeout(dibujar, 1000);
	var radio = document.getElementById("formSitios:radio");
	 $(radio).keydown(function (e) {
	        // Allow: backspace, delete, tab, escape, enter and .
	        if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
	             // Allow: Ctrl+A
	            (e.keyCode == 65 && e.ctrlKey === true) ||
	             // Allow: Ctrl+C
	            (e.keyCode == 67 && e.ctrlKey === true) ||
	             // Allow: Ctrl+X
	            (e.keyCode == 88 && e.ctrlKey === true) ||
	             // Allow: home, end, left, right
	            (e.keyCode >= 35 && e.keyCode <= 39)) {
	                 // let it happen, don't do anything
	                 return;
	        }
	        // Ensure that it is a number and stop the keypress
	        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
	            e.preventDefault();
	        }
	    });
});

var contadorMarkers = 0;
var punto = null;
var drawingManager = null;
var latPosicion = null;
var lngPosicion = null;
var markers = new Array();
var ventanas = new Array();

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
	PF('sitio_gmap').getMap().setCenter(new google.maps.LatLng(latitude, longitude));
	var imagen = {
		    url: "../resources/imagenes/logoCycle.png",
		    scaledSize: new google.maps.Size(30, 30)
		};
    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(latitude, longitude),
        map: PF('sitio_gmap').getMap(),
        icon: imagen
      });
	var informacion = new google.maps.InfoWindow({
        content: "Esta aquí!!"
    });
	google.maps.event.addListener(marker, 'click', function () {
		informacion.open(PF('sitio_gmap').getMap(),marker);
    });
	informacion.open(PF('sitio_gmap').getMap(),marker);
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
	drawingManager.setMap(PF('sitio_gmap').getMap());
	google.maps.event.addListener(drawingManager, 'markercomplete', function(newMarker) {
		if (contadorMarkers==0){
			newMarker.setLabel("S");	
		}
		punto = newMarker;
		capturarCoordenadas();
		drawingManager.setMap(PF('sitio_gmap').getMap());
		google.maps.event.addListener(newMarker,'dragend',function(evento) {
			if (evento && evento.latLng){
				capturarCoordenadas();
			}
	    });
		contadorMarkers++;
		if (contadorMarkers==1){
			drawingManager.setDrawingMode(null);
			drawingManager.drawingControl = false;
		}
	});
}

function capturarCoordenadas(){
	if (punto){
		lat=document.getElementById('formSitios:lat');
		lng=document.getElementById('formSitios:lng');
		if (lat){
			lat.value=punto.getPosition().lat();
		}
		if (lng){
			lng.value=punto.getPosition().lng();
		}
	}
}

function limpiarMapa(){
	if (punto){
		lat=document.getElementById('formSitios:lat');
		lng=document.getElementById('formSitios:lng');
		otro=document.getElementById('formSitios:otro');
		radio=document.getElementById('formSitios:radio');
		if (lat){
			lat.value="";
		}
		if (lng){
			lng.value="";
		}
		if (otro){
			otro.value="";
		}
		if (radio){
			radio.value="";
		}
		punto.setMap(null);
		contadorMarkers = 0;
	}
	if (drawingManager){
		drawingManager.drawingControl = true;
		drawingManager.setMap(PF('sitio_gmap').getMap());	
		drawingManager.setDrawingMode(google.maps.drawing.OverlayType.MARKER);
	}
	document.getElementById("formSitios:json_sitios").value="";
	for (var i=0;i<markers.length;i++){
		markers[i].setMap(null);
	}
	markers = new Array();
	ventanas = new Array();
	repintar();
}

function repintar(){
	if (latPosicion && lngPosicion){
		setMapCenter(latPosicion,lngPosicion);
	}
}

function pintarSitios(){
	for (var i=0;i<markers.length;i++){
		markers[i].setMap(null);
	}
	markers = new Array();
	ventanas = new Array();
	var sitios = document.getElementById("formSitios:json_sitios");
	var listSitios = $(sitios).val();
	listSitios = jQuery.parseJSON(listSitios);
	ventanas = new Array();
	for (var i=0;i<listSitios.length;i++){
		var marker = new google.maps.Marker({
			map: PF('sitio_gmap').getMap(),
			position: new google.maps.LatLng(listSitios[i].latitud, listSitios[i].longitud)
		});
		var infowindow = new google.maps.InfoWindow();
		infowindow.setContent(listSitios[i].nombre+"<br/>"+listSitios[i].direccion);
		ventanas[i] = infowindow; 
		
		google.maps.event.addListener(marker, 'click', (function (marker, i) {
	    	return function(){
	    		ventanas[i].open(PF('sitio_gmap').getMap(), marker);
	    	};
	    })(marker,i));
		
		markers.push(marker);
	}
}