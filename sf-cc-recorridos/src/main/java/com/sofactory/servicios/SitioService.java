package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.RespuestaSitioDTO;
import com.sofactory.negocio.interfaces.SitioBeanLocal;

@Path("sitio")
public class SitioService {

	private static String servicioRegistrarServicio = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/registrarServicio";
	
	@EJB
	private SitioBeanLocal sitioBeanLocal;
	
	@GET
	@Path("encontrarSitios/{lat}/{lng}/{radio}/{sitio}/{codigoUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaSitioDTO encontrarSitios(
			@PathParam("lat")String lat, 
			@PathParam("lng")String lng,
			@PathParam("radio")String radio, 
			@PathParam("sitio")String sitio,
			@PathParam("codigoUsuario")String codigoUsuario) {
		RespuestaSitioDTO respuestaSitioDTO = new RespuestaSitioDTO();
		try {
			return sitioBeanLocal.encontrarSitios(lat, lng, radio, sitio);
		} catch (Exception e) {
			respuestaSitioDTO = new RespuestaSitioDTO();
			respuestaSitioDTO.setCodigo(1);
			respuestaSitioDTO.setMensaje("Hubo algun error en encontrar los sitios");
			e.printStackTrace();
		}
		return respuestaSitioDTO;
	}
}