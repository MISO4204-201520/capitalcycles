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
  	<form id="signup" method="POST" action="enviarMensajeService.php">
		<h1>Enviar mensaje</h1>
		<input type="text" placeholder="Nombre de Usuario" name="login" id="login" value="<?php echo $_GET['loginpara']; ?>" disabled>
		<input type="hidden" placeholder="Codigo de Usuario" name="codigopara" id="codigopara" value="<?php echo $_GET['usrpara']; ?>">
		<input type="text" placeholder="Texto del mensaje" name="texto" id="texto">
		<button type="submit">Enviar</button>	
	</form>
	
  </body>

</html>