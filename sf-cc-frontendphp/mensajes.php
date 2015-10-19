<?php 
	session_start();
?>
<!doctype html>
<html>
  <head>
    <title>CapitalCycles</title>
    <meta charset="utf-8" />
	<link rel="stylesheet" href="css/style.css">
  </head>

  <body>
	<?php include('menu.php'); ?>
	<?php include('menuMensajes.php'); ?>
	<br/>
	
  	<form id="signup">
		<h1>Mensajes recibidos</h1>
		<?php
			$url = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/mensajesRecibidosPorUsuario/" . $_SESSION['codigoUsuario'];
			
			$jsonMensajes = file_get_contents($url);
			if($jsonMensajes->codigo == "0")
			{
				foreach($jsonMensajes->mensajes as $mensaje)
				{
					$urlBuscarUsuarios = "/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarUsuarioPorCodigo/" . $mensaje->usrdesde;
					$jsonUsuarioDesde = file_get_contents($urlBuscarUsuarios);
					echo "<p>" . $jsonUsuarioDesde->nombres . " " . $jsonUsuarioDesde->apellidos . " (<b>" . $jsonUsuarioDesde->login . "</b>) </p>";
					echo "<p> dice: " . $mensaje->texto . "</p>";
				}
			}
			
		?>
	</form>
	
  </body>

</html>