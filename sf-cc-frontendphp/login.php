<?php 
	session_start();
  ?>
<!doctype html>

<?php 
	if(isset($_SESSION['loginUsuario']))
		session_destroy();
  ?>
<html>
  <head>
    <title>CapitalCycles</title>
    <meta charset="utf-8" />
	<link rel="stylesheet" href="css/style.css">
  </head>

  <body>

  	<form id="signup" method="POST" action="loginservice.php">
		<h1>Bienvenido a CapitalCycles!</h1>
		<input type="text" placeholder="Nombre de Usuario" name="login" id="login" required="">
		<input type="password" placeholder="Contrase&ntilde;a" name="password" id="password" required="">				
		<button type="submit">Iniciar sesi&oacute;n</button>	
	</form>
	
  </body>

</html>