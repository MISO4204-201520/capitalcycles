package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaDTO;
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
			respuestaSitioDTO = sitioBeanLocal.encontrarSitios(lat, lng, radio, sitio);
		
			if (respuestaSitioDTO!=null && respuestaSitioDTO.getSitios()!=null && !respuestaSitioDTO.getSitios().isEmpty()){
				// Inicio Otorga Puntos por Fidelizacion
				RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
				registrarPuntosDTO.setCodigoUsuario(codigoUsuario);
				registrarPuntosDTO.setServicio("encontrarSitios");
			
				Client client = ClientBuilder.newClient();
				WebTarget targetMensaje = client.target(servicioRegistrarServicio);
				RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);
				// Fin Otorga Puntos por Fidelizacion
			}
			return respuestaSitioDTO;
		} catch (Exception e) {
			respuestaSitioDTO = new RespuestaSitioDTO();
			respuestaSitioDTO.setCodigo(1);
			respuestaSitioDTO.setMensaje("Hubo algun error en encontrar los sitios");
			e.printStackTrace();
		}
		return respuestaSitioDTO;
	}
}