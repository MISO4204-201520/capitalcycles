<?php

$password = $_POST['password'];
$login = $_POST['login'];

//Encriptar contraseña
$salt = "llavePassword";
$credencial = crypt($password,$salt);

$data = array(
	"login" => $login,
	"credencial" => $password);

$url = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/esValidoUsuario"
$curl = curl_init();
 
curl_setopt($curl, CURLOPT_POST, 1);
// if ($data)
curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

// Buscar al usuario no requiere autenticación
// curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
// curl_setopt($curl, CURLOPT_USERPWD, "username:password");

curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

$result = curl_exec($curl);
$msj = $result->mensaje;
if($result->codigo = "")
	echo '<script>alert("Inicio de sesión exitoso");</script>';
else
	echo '<script>alert("' . $msj . '");</script>';

curl_close($curl);

?>