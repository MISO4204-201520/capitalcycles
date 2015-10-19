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
		<h1>Enviar mensaje a:</h1>
		<?php
			$url = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarTodosUsuarios";
			
			$jsonUsuarios = file_get_contents($url);
			if($jsonUsuarios->codigo == "0")
			{
				foreach($jsonUsuarios->usuarios as $usuario)
				{
					echo "<a href=\"enviarMensaje.php?usrpara=" . $usuario->codigo . "&loginpara=" . $usuario->login . "\">" . $usuario->nombres . " " . $usuario->apellidos . " (<b>" . $usuario->login . "</b>) </a>";
					echo "<br/>";
				}
			}
		?>
	</form>
	
  </body>

</html>