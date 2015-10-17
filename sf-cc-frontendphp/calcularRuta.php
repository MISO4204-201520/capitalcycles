<html>
	<head>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
		<title>CapitalCycles</title>
		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
		<style type="text/css">
			#map_canvas { 
				height: 88%;
			}
		</style>
		<link rel="stylesheet" src="css/style.css">
		<script type="text/javascript">
		var directionDisplay;
		var directionsService = new google.maps.DirectionsService();
		var map;
		
		function initialize() {
			directionsDisplay = new google.maps.DirectionsRenderer();
			var bogota = new google.maps.LatLng(4.598889, -74.0808);
			var myOptions = {
				zoom:15,
				mapTypeId: google.maps.MapTypeId.ROADMAP,
				center: bogota
			}

			map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
			directionsDisplay.setMap(map);
		}

		function calcRoute() {
			var start = document.getElementById("start").value;
			var end = document.getElementById("end").value;
			var distanceInput = document.getElementById("distance");
			
			var request = {
				origin:start, 
				destination:end,
				travelMode: google.maps.TravelMode.DRIVING
			};
			
			directionsService.route(request, function(response, status) {
				if (status == google.maps.DirectionsStatus.OK) {
					directionsDisplay.setDirections(response);
					distanceInput.value = response.routes[0].legs[0].distance.value / 1000;
				}
			});
		}
		</script>
	</head>
	<body onload="initialize()">
		<?php include ('menu.php'); ?>
		<div>
			<p>
				<label for="start">Direcci&oacute;n inicial: </label>
				<input type="text" name="start" id="start" />
				
				<label for="end">Direcci&oacute;n final: </label>
				<input type="text" name="end" id="end" />
				
				<input type="submit" value="Calcular Ruta" onclick="calcRoute()" />
			</p>
			<p>
				<label for="distance">Distancia calculada (km): </label>
				<input type="text" name="distance" id="distance" readonly="true" disabled />
			</p>
		</div>
		<div id="map_canvas"></div>
	</body>
</html>