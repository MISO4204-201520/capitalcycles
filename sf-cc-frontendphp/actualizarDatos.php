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

  	<form id="signup" method="POST" action="actualizardatosservice.php">
		<h1>Actualiza tus datos</h1>
		<input type="text" placeholder="Nombre de Usuario" name="login" id="login" value="<?php echo $_SESSION['loginUsuario']; ?>" required="" disabled>
		<input type="text" placeholder="Nombres" name="name" id="name" >
		<input type="text" placeholder="Apellidos" name="lastname" id="lastname" >
		<input type="number" placeholder="Celular" name="phone" id="phone" >
		<input type="email" placeholder="Email" name="email" id="email" >
		G&eacute;nero: <select id="genero" name="genero" form="signup" >
			<option value="M">Masculino</option>
			<option value="F">Femenino</option>
		</select>
		<button type="submit">Actualizar</button>	
	</form>
	
  </body>
</html>
<?php 
}
?>
