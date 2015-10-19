<?php

session_start();

// $password = $_POST['password'];
// $login = $_POST['login'];

$usrpara = $_POST['codigopara'];
$usrdesde = $_SESSION['codigoUsuario'];
$texto = $_POST['texto'];


$data = array(
	"usrdesde" => $usrdesde,
	"usrpara" => $usrpara,
	"texto" => $texto);

$url = "http://localhost:8080/http://localhost:8080/sf-cc-mensajes/rest/mensajeService/crearNuevoMensaje";
$curl = curl_init();
 
curl_setopt($curl, CURLOPT_POST, 1);
curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

$result = curl_exec($curl);
$msj = $result->mensaje;
if($result->codigo = 0){
	echo '<script>alert("Mensaje enviado correctamente"); window.location="mensajes.php";</script>';
}
else
	echo '<script>alert("' . $msj . '"); window.location="login.php";</script>';

curl_close($curl);

?>