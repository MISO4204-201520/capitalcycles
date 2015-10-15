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

  	<form id="signup" method="POST" action="register.php">
		<h1>Bienvenido a CapitalCycles!</h1>
		<input type="text" placeholder="Nombres" name="name" id="name" required="">
		<input type="text" placeholder="Apellidos" name="lastname" id="lastname" required="">
		<input type="number" placeholder="Celular" name="phone" id="phone" required="">
		<input type="email" placeholder="Email" name="email" id="email" required="">
		<input type="text" placeholder="Nombre de Usuario" name="login" id="login" required="">
		G&eacute;nero: <select id="genero" name="genero" form="signup" >
			<option value="M">Masculino</option>
			<option value="F">Femenino</option>
		</select>
		<input type="password" placeholder="Contrase&ntilde;a" name="password" id="password" required="">
		<input type="password" placeholder="Confirmar Contrase&ntilde;a" name="pwdconfirm" id="pwdconfirm" required="">					
		<button type="submit">Crear cuenta</button>	
	</form>
	
  </body>

</html>