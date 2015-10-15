<?php 
session_start();
if(!isset($_SESSION['loginUsuario']))
{
	echo "<script>alert('No ha iniciado sesión.'); window.location='login.php';</script>"
}
else
{
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
	<br/>

  	<form id="signup" method="POST" action="cambiarcredencialservice.php">
		<h1>Cambia tu contrase&ntilde;a</h1>
		<input type="text" placeholder="Nombre de Usuario" name="login" id="login" value="<?php echo $_SESSION['loginUsuario']; ?>" required="" disabled>
		<input type="password" placeholder="Contrase&ntilde;a Actual" name="oldpassword" id="oldpassword" required="">				
		<input type="password" placeholder="Nueva Contrase&ntilde;a" name="newpassword" id="newpassword" required="">
		<input type="password" placeholder="Confirmar Nueva Contrase&ntilde;a" name="confirmpassword" id="confirmpassword" required="">
		<button type="submit">Actualizar credencial</button>
	</form>
	
  </body>

</html>
<?php 
}
?>