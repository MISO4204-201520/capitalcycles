<?php

$email = $_POST['email'];
$password = $_POST['password'];
$nombres = $_POST['nombres'];
$apellidos = $_POST['apellidos'];
$login = $_POST['login'];
$celular = $_POST['phone'];
$genero = $_POST['genero'];
$roles = array(1,2);

//Encriptar contraseña
$salt = "llavePassword";
$credencial = crypt($password,$salt);

$data = array(
	"login" => $login,
	"credencial" => $password,
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
if($result->codigo = "")
	echo '<script>alert("Registro exitoso");</script>';
else
	echo '<script>alert("' . $msj . '");</script>';

curl_close($curl);

?>