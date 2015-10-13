<?php
session_start();

if(!$_SESSION['loginUsuario'] || !$_SESION['encCredencial'] || !$_SESSION['codigoUsuario'])
{
	echo "<script>alert('Debe iniciar sesión.');
			window.location = 'login.php';</script>"
}
else
{
	$oldpassword = $_POST['oldpassword'];
	$newpassword = $_POST['newpassword'];
	$confirmpassword = $_POST['confirmpassword'];
	$login = $_POST['login'];

	//Encriptar contraseña
	// $salt = "llavePassword";
	// $credencial = crypt($password,$salt);

	$urlencriptar = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/encriptar"
	$servicio_encriptar = curl_init();
	curl_setopt($servicio_encriptar, CURLOPT_POST, 1);
	curl_setopt($servicio_encriptar, CURLOPT_URL, $urlencriptar);
	curl_setopt($servicio_encriptar, CURLOPT_RETURNTRANSFER, 1);

	$dataEncriptar = array("credencial" => $oldpassword);
	curl_setopt($servicio_encriptar, CURLOPT_POSTFIELDS, $dataEncriptar);
	$resultEncripcion = curl_exec($servicio_encriptar);
	$oldcredencial = $resultEncripcion->credencial;

	$dataEncriptar2 = array("credencial" => $newpassword);
	curl_setopt($servicio_encriptar, CURLOPT_POSTFIELDS, $dataEncriptar2);
	$resultEncripcion2 = curl_exec($servicio_encriptar);
	$newcredencial = $resultEncripcion2->credencial;

	$dataEncriptar3 = array("credencial" => $confirmpassword);
	curl_setopt($servicio_encriptar, CURLOPT_POSTFIELDS, $dataEncriptar3);
	$resultEncripcion3 = curl_exec($servicio_encriptar);
	$confirmcredencial = $resultEncripcion3->credencial;

	curl_close($servicio_encriptar);

	$data = array(
		"login" => $login,
		"credencial" => $oldcredencial,
		"credencialNueva" => $newcredencial,
		"confirmacionCredencialNueva" => $confirmcredencial);

	$url = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/cambiarCredencial"
	$curl = curl_init();
	 
	curl_setopt($curl, CURLOPT_POST, 1);

	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

	curl_setopt($curl, CURLOPT_URL, $url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

	$result = curl_exec($curl);
	$msj = $result->mensaje;
	if($result->codigo = 0)
		echo '<script>alert("Actualización de credencial exitosa");</script>';
	else
		echo '<script>alert("' . $msj . '");</script>';

	curl_close($curl);
}
?>