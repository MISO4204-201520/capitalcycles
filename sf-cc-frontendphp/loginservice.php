<?php

$password = $_POST['password'];
$login = $_POST['login'];

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
	"credencial" => $credencial);

$url = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/esValidoUsuario";
$curl = curl_init();
 
curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

$result = curl_exec($curl);
$msj = $result->mensaje;
if($result->codigo = 0){
	session_start();
	$_SESSION['encCredencial'] = $credencial;
	$_SESSION['loginUsuario'] = $login;
	$_SESSION['codigoUsuario'] = $result->codigoUsuario;
	echo '<script>alert("Inicio de sesión exitoso");</script>';
}
else
	echo '<script>alert("' . $msj . '");</script>';

curl_close($curl);

?>