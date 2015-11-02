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

import com.google.maps.model.LatLng;
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
	@Path("encontrarMejor/{latInicio}/{lngInicio}/{latFin}/{lngFin}/{codigoUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RutaDTO encontrarMejor(
			@PathParam("latInicio")String latInicio, 
			@PathParam("lngInicio")String lngInicio,
			@PathParam("latFin")String latFin, 
			@PathParam("lngFin")String lngFin,
			@PathParam("codigoUsuario")String codigoUsuario) {
		RutaDTO respuesta = new RutaDTO();
		Ruta ruta;
		try {
			LatLng latLngInicio = new LatLng(Double.parseDouble(latInicio), Double.parseDouble(lngInicio));
			LatLng latLngFin = new LatLng(Double.parseDouble(latFin), Double.parseDouble(lngFin));
			
			Posicion origen = new Posicion();
			origen.setLatitud(latLngInicio.lat);
			origen.setLongitud(latLngInicio.lng);

			Posicion destino = new Posicion();
			destino.setLatitud(latLngFin.lat);
			destino.setLongitud(latLngFin.lng);    

			ruta = rutaBeanLocal.encontrarMejor(origen, destino, codigoUsuario);
			
			respuesta.setId(ruta.getId());
			respuesta.setPosiciones(new ArrayList<>());
			respuesta.setDireccionInicio(ruta.getDireccionInicio());
			respuesta.setDireccionFin(ruta.getDireccionFin());
			respuesta.setDistanciaVisualizacion(ruta.getDistanciaVisualizacion());
			respuesta.setTiempoTotalVisualizacion(ruta.getTiempoTotalVisualizacion());
			
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