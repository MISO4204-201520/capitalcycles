<?php

$email = $_POST['email'];
$password = $_POST['password'];
$nombres = $_POST['nombres'];
$apellidos = $_POST['apellidos'];
$login = $_POST['login'];
$celular = $_POST['phone'];
$genero = $_POST['genero'];
$roles = array("id" => 2);

//Encriptar contraseña
// $salt = "llavePassword";
// $credencial = crypt($password,$salt);

//Encripto la credencial
$urlencriptar = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/encriptar"
$servicio_encriptar = curl_init();
curl_setopt($servicio_encriptar, CURLOPT_POST, 1);
$dataEncriptar = array("credencial" => $password);
curl_setopt($servicio_encriptar, CURLOPT_POSTFIELDS, $dataEncriptar);
$resultEncripcion = curl_exec($servicio_encriptar);
$credencial = $resultEncripcion->credencial;

$data = array(
	"login" => $login,
	"credencial" => $credencial,
	"nombres" => $nombres,
	"apellidos" => $apellidos,
	"celular" => $celular,
	"genero" => $genero,
	"correo" => $email,
	"roles" => $roles);

$url = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/crear"
$curl = curl_init();
 
curl_setopt($curl, CURLOPT_POST, 1);
// if ($data)
curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

// Registrar usuario no requiere autenticación
// curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);
// curl_setopt($curl, CURLOPT_USERPWD, "username:password");

curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

$result = curl_exec($curl);
$msj = $result->mensaje;
if($result->codigo = ""){
	echo '<script>alert("Registro exitoso");</script>';
	$_SESSION['encCredencial'] = $credencial;
	$_SESSION['loginUsuario'] = $login;
	$_SESSION['codigoUsuario'] = $result->codigoUsuario;
}
else
	echo '<script>alert("' . $msj . '");</script>';

curl_close($curl);

?>