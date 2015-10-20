<?php 
	session_start();

	$url= "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/redimirProducto";
	$idProd = $_GET['prod'];
	$idUsr = $_SESSION['codigoUsuario'];
	$data = array(
		"idProducto" => $idProd,
		"codigoUsuario" => $idUsr);
	
	$curl = curl_init();
	
	curl_setopt($curl, CURLOPT_POST, 1);
	curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

	curl_setopt($curl, CURLOPT_URL, $url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
	
	$result = curl_exec($curl);
	$msj = $result->mensaje;
	
	if($result->codigo = 0){
		echo '<script>alert("Felicitaciones, has adquirido el producto!"); window.close();</script>';
	}
	else
		echo '<script>alert("' . $msj . '"); window.close();</script>';

	curl_close($curl);

?>