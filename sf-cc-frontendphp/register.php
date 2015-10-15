<?php

$email = $_POST['email'];
$password = $_POST['password'];
$nombres = $_POST['nombres'];
$apellidos = $_POST['apellidos'];
$login = $_POST['login'];
$celular = $_POST['phone'];
$genero = $_POST['genero'];
$roles = array("id" => 2);

//Encripto la credencial
$urlencriptar = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/encriptar";
$servicio_encriptar = curl_init();
curl_setopt($servicio_encriptar, CURLOPT_POST, 1);
$dataEncriptar = array("credencial" => $password);
curl_setopt($servicio_encriptar, CURLOPT_POSTFIELDS, $dataEncriptar);
curl_setopt($servicio_encriptar, CURLOPT_URL, $urlencriptar);
curl_setopt($servicio_encriptar, CURLOPT_RETURNTRANSFER, 1);
$resultEncripcion = curl_exec($servicio_encriptar);
$credencial = $resultEncripcion->credencial;

curl_close($servicio_encriptar);

$data = array(
	"login" => $login,
	"credencial" => $credencial,
	"nombres" => $nombres,
	"apellidos" => $apellidos,
	"celular" => $celular,
	"genero" => $genero,
	"correo" => $email,
	"roles" => $roles);

$url = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/crear";
$curl = curl_init();
 
curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

$result = curl_exec($curl);
$msj = $result->mensaje;
if($result->codigo = 0){
	$_SESSION['encCredencial'] = $credencial;
	$_SESSION['loginUsuario'] = $login;
	$_SESSION['codigoUsuario'] = $result->codigoUsuario;
	echo '<script>alert("Registro exitoso"); window.location="calcularRuta.php";</script>';
}
else
	echo '<script>alert("' . $msj . '"); window.location="index.php";</script>';

curl_close($curl);

?>