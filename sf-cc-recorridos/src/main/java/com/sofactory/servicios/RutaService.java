package com.sofactory.servicios;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.sofactory.dtos.PosicionDTO;
import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.dtos.RutaDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Posicion;
import com.sofactory.entidades.Ruta;
import com.sofactory.negocio.interfaces.RutaBeanLocal;

@Path("ruta")
public class RutaService {

	private static String servicioRegistrarServicio = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/registrarServicio";
	
	@EJB
	private RutaBeanLocal rutaBeanLocal;

	@GET
	@Path("encontrarMejor/{inicio}/{fin}/{codigoUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RutaDTO encontrarMejor(
			@PathParam("inicio")String inicio, 
			@PathParam("fin")String fin, 
			@PathParam("codigoUsuario")String codigoUsuario) {
		RutaDTO respuesta = new RutaDTO();
		Ruta ruta;
		try {
			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBEelLTtoKYBwxxjM9nlkipXI4bO4BOK3g");
			GeocodingResult[] results;

			results = GeocodingApi.geocode(context,
					inicio).await();

			Posicion origen = new Posicion();
			origen.setLatitud(results[0].geometry.location.lat);
			origen.setLongitud(results[0].geometry.location.lng);

			results =  GeocodingApi.geocode(context,
					fin).await();

			Posicion destino = new Posicion();
			destino.setLatitud(results[0].geometry.location.lat);
			destino.setLongitud(results[0].geometry.location.lng);    

			ruta = rutaBeanLocal.encontrarMejor(origen, destino, codigoUsuario);
			
			respuesta.setId(ruta.getId());
			respuesta.setPosiciones(new ArrayList<>());
			
			PosicionDTO posicionDto;
			for (Posicion posicion : ruta.getPosiciones()) {
				posicionDto = new PosicionDTO(
						posicion.getLatitud(), posicion.getLongitud(), null);
				respuesta.getPosiciones().add(posicionDto);
			}
			
			respuesta.setMensaje("OK");
			
			// Inicio Otorga Puntos por Fidelizacion
			
			RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setCodigo(new Long(codigoUsuario));
			registrarPuntosDTO.setCodigoUsuario(usuarioDTO.getCodigo().toString());
			registrarPuntosDTO.setServicio("encontrarMejor");
		
			Client client = ClientBuilder.newClient();
			WebTarget targetMensaje = client.target(servicioRegistrarServicio);
			RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);		
			
			// Fin Otorga Puntos por Fidelizacion
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setMensaje("ERROR-"+e.getMessage());
		}

		return respuesta;
	}
}