<?php 
	session_start();
?>
<!doctype html>
<html>
  <head>
    <title>CapitalCycles</title>
    <meta charset="utf-8" />
	<link rel="stylesheet" href="css/style.css">
	<script type="text/javascript">
		function confirmarProducto(idProducto)
		{
			if(confirm('¿Seguro que desea redimir sus puntos por este producto?'))
			{
				window.open('producto.php?prod' + idProducto)
			}
		}
	</script>
  </head>

  <body>
	<?php include('menu.php'); ?>
	<?php include('menuMensajes.php'); ?>
	<br/>
	
  	<form id="signup">
		<h1>Cat&aacute;logo de productos pare redimir tus puntos</h1>
		
		<?php
			$urlPuntos = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/obtenerPuntoUsuario/" . $_SESSION['codigoUsuario'];
			$jsonPuntos = file_get_contents($urlPuntos);
			if($jsonPuntos->codigo == "0")
			{
				echo "<h1>Actualmente tienes " . $jsonPuntos->puntos " puntos.</h1>";
			}
		?>
		
		<?php
			$url = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/catalogoProductos/" . $_SESSION['codigoUsuario'];
			
			$jsonCatalogo = file_get_contents($url);
			if($jsonCatalogo->codigo == "0")
			{
				foreach($jsonCatalogo->productos as $producto)
				{
					// echo "<h3><a href=\"producto.php?prod=" . $producto->id . "\"" . $producto->nombre . "</h3>";
					echo "<h3>" . $producto->nombre . "</h3>";
					echo "<p> Ll&eacute;valo por: " . $producto->puntos . " puntos!</p>";
					echo "<button type=\"button\" onclick=\"confirmarProducto(" . $producto->id . ")\">Redimir puntos</button>";
					echo "<br/>";
				}
			}
			
		?>
	</form>
	
  </body>

</html>