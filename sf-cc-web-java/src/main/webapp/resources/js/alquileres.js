$( document ).ready(function() {
	posicion();
	setTimeout(dibujar, 1000);
});

var contadorMarkers = 0;
var punto = null;
var drawingManager = null;
var latPosicion = null;
var lngPosicion = null;
var markers = new Array();
var ventanas = new Array();
var marker = null;

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
	lat=document.getElementById('formAlquileres:lat');
	lng=document.getElementById('formAlquileres:lng');
	if (lat){
		lat.value=latitude;
	}
	if (lng){
		lng.value=longitude;
	}
 	if (latPosicion!=null && lngPosicion != null){
 		drawingManager.setDrawingMode(null);
 		drawingManager.setOptions({
 			  drawingControl: false
 			});	
 	}
	var imagen = {
		    url: "../resources/imagenes/logoCycle.png",
		    scaledSize: new google.maps.Size(30, 30)
		};
    marker = new google.maps.Marker({
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
	if (drawingManager==null){
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
}

function capturarCoordenadas(){
	if (punto){
		lat=document.getElementById('formAlquileres:lat');
		lng=document.getElementById('formAlquileres:lng');
		if (lat){
			lat.value=punto.getPosition().lat();
		}
		if (lng){
			lng.value=punto.getPosition().lng();
		}
	}
}

function limpiarMapa(){
	
	lat=document.getElementById('formAlquileres:lat');
	lng=document.getElementById('formAlquileres:lng');
	if (lat){
		lat.value="";
	}
	if (lng){
		lng.value="";
	}
	if (punto){
		punto.setMap(null);
		contadorMarkers = 0;
	}
	PF('sitio_gmap').getMap().setCenter(new google.maps.LatLng(4.598889, -74.0808));
	if (drawingManager){
		drawingManager.drawingControl = true;
		drawingManager.setMap(PF('sitio_gmap').getMap());	
		drawingManager.setDrawingMode(google.maps.drawing.OverlayType.MARKER);
	}
	if (marker){
		marker.setMap(null);
		marker = null;
	}
	document.getElementById("formAlquileres:json_alquileres").value="";
	for (var i=0;i<markers.length;i++){
		markers[i].setMap(null);
	}
	markers = new Array();
	ventanas = new Array();
	
	posicion();
	setTimeout(dibujar, 1000);
	repintar();
}

function repintar(){
	if (latPosicion && lngPosicion){
		setMapCenter(latPosicion,lngPosicion);
	}
}

function pintarSitios(){
	document.getElementById("formAlquileres:nombreSitio").value = "";
	for (var i=0;i<markers.length;i++){
		markers[i].setMap(null);
	}
	markers = new Array();
	ventanas = new Array();
	var sitios = document.getElementById("formAlquileres:json_alquileres");
	if (sitios && sitios.value && sitios.value!=""){
		var listSitios = $(sitios).val();
		listSitios = jQuery.parseJSON(listSitios);
		ventanas = new Array();
		for (var i=0;i<listSitios.length;i++){
			var marker = new google.maps.Marker({
				map: PF('sitio_gmap').getMap(),
				position: new google.maps.LatLng(listSitios[i].latitud, listSitios[i].longitud)
			});
			var infowindow = new google.maps.InfoWindow();
			infowindow.setContent("<div style='cursor: pointer;text-decoration:underline;' onclick='clickAlquiler("+listSitios[i].nombre+");'>"+listSitios[i].nombre+"</div><br/>"+listSitios[i].direccion);
			ventanas[i] = infowindow; 
			
			google.maps.event.addListener(marker, 'click', (function (marker, i) {
		    	return function(){
		    		ventanas[i].open(PF('sitio_gmap').getMap(), marker);
		    	};
		    })(marker,i));
			
			markers.push(marker);
		}
	}
}

function clickAlquiler(nombre){
	document.getElementById("formAlquileres:nombreSitio").value = nombre;
	var boton = $(document.getElementById("formAlquileres:botonAlquiler"));
	if (boton){
		boton.click();
	}
}