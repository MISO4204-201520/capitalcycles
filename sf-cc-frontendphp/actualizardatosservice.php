<?php

if(!$_SESSION['loginUsuario'] || !$_SESION['encCredencial'] || !$_SESSION['codigoUsuario'])
{
	echo "<script>alert('Debe iniciar sesión.');
			window.location = 'login.php';</script>"
}
else
{

	$codigoUsuario = $_SESSION['codigoUsuario'];
	$email = $_POST['email'];
	$password = $_POST['credencial'];
	$nombres = $_POST['nombres'];
	$apellidos = $_POST['apellidos'];
	$login = $_SESSION['loginUsuario'];
	$celular = $_POST['phone'];
	$genero = $_POST['genero'];
	$roles = array("id" => 2);

	//Encriptar contraseña
	// $salt = "llavePassword";
	// $credencial = crypt($password,$salt);

	//Encripto la credencial
	// $urlencriptar = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/encriptar"
	// $servicio_encriptar = curl_init();
	// curl_setopt($servicio_encriptar, CURLOPT_POST, 1);
	// $dataEncriptar = array("credencial" => $password);
	// curl_setopt($servicio_encriptar, CURLOPT_POSTFIELDS, $dataEncriptar);
	// $resultEncripcion = curl_exec($servicio_encriptar);
	// $credencial = $resultEncripcion->credencial;

	$data = array(
		"codigo" => $codigoUsuario;
		"login" => $login,
		"credencial" => $password,
		"nombres" => $nombres,
		"apellidos" => $apellidos,
		"celular" => $celular,
		"genero" => $genero,
		"correo" => $email,
		"roles" => $roles);

	$url = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/actualizar"
	$curl = curl_init();
	 
	curl_setopt($curl, CURLOPT_POST, 1);
	// if ($data)
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

	curl_setopt($curl, CURLOPT_URL, $url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

	$result = curl_exec($curl);
	$msj = $result->mensaje;
	if($result->codigo = 0)
		echo '<script>alert("Actualización exitosa.");</script>';
	else
		echo '<script>alert("' . $msj . '");</script>';

	curl_close($curl);
}

?>