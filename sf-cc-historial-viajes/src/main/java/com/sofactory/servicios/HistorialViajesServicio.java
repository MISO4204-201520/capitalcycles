package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.PosicionDTO;
import com.sofactory.dtos.RegistrarPosicionDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.dtos.RutaDTO;
import com.sofactory.entidades.PosicionTiempo;
import com.sofactory.negocio.interfaces.DesplazamientoBeanLocal;
import com.sofactory.negocio.interfaces.HistorialViajesBeanLocal;

@Path("desplazamiento")
public class HistorialViajesServicio {

	@EJB
	private HistorialViajesBeanLocal historialViajesBean;

	@GET
	@Path("getTodosViajesUsuario/{cod}")
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaDTO getTodosViajesUsuario(@PathParam("cod") long codUsuario){

		HistorialViajesDTO respuestaHistorialDTO = new HistorialViajesDTO("0");
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resu.getCodigo());
		System.out.println("RESU "+resu.getMensaje());
		
		if (resu.getCodigo()==0){
		
			try {
	
				List<Ruta> rutas = historialViajesBean.getTodosViajes(Long.toString(codUsuario));
				
				for  (Ruta r: rutas){
					RutaDTO rutadto = new RutaDTO(r.getId(), r.getCodigoUsuario());
					respuestaHistorialDTO.getRutas().add(rutadto);
				}
				
				if (rutas.size()>0){
	
//					// Inicio Otorga Puntos por Fidelizacion		
//					
//					RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
//					registrarPuntosDTO.setCodigoUsuario(Long.toString(codUsuario));
//					registrarPuntosDTO.setServicio("");
//				
//					client = ClientBuilder.newClient();
//					targetMensaje = client.target(servicioRegistrarServicio);
//					RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);			
//				
//					// Fin Otorga Puntos por Fidelizacion				
//
				}
			} catch (Exception e) {
	
				respuestaHistorialDTO.setCodigo(1);
				respuestaHistorialDTO.setMensaje("Hubo un error en el sistema");
				e.printStackTrace();
				
			}
		}else{
			respuestaHistorialDTO.setCodigo(10);
			respuestaHistorialDTO.setMensaje(resu.getMensaje());	
		}
			
		return respuestaHistorialDTO;
	}
	
//	@GET
//	@Path("getViajesRangoFechas/{cod}/{fecha_ini}/{fecha_fin}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public RespuestaDTO getViajesRangoFechas(@PathParam("cod") long codUsuario,
//			@PathParam("fecha_ini") String fechaInicial, @PathParam("fecha_fin") String fechaFinal){
//
//		HistorialViajesDTO respuestaHistorialDTO = new HistorialViajesDTO("0");
//	
//		Client client = ClientBuilder.newClient();
//		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
//		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
//		
//		System.out.println("RESU "+resu.getCodigo());
//		System.out.println("RESU "+resu.getMensaje());
//		
//		if (resu.getCodigo()==0){
//		
//			try {
//	
//				List<Ruta> rutas = historialViajesBean.getViajesRangoFechas(Long.toString(codUsuario),fechaInicial, fechaFinal);
//				
//				for  (Ruta r: rutas){
//					RutaDTO rutadto = new RutaDTO(r.getId(), r.getCodigoUsuario());
//					respuestaHistorialDTO.getRutas().add(rutadto);
//				}
//				
//				if (rutas.size()>0){
//	
////					// Inicio Otorga Puntos por Fidelizacion		
////					
////					RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
////					registrarPuntosDTO.setCodigoUsuario(Long.toString(codUsuario));
////					registrarPuntosDTO.setServicio("");
////				
////					client = ClientBuilder.newClient();
////					targetMensaje = client.target(servicioRegistrarServicio);
////					RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);			
////				
////					// Fin Otorga Puntos por Fidelizacion				
////
//				}
//			} catch (Exception e) {
//	
//				respuestaHistorialDTO.setCodigo(1);
//				respuestaHistorialDTO.setMensaje("Hubo un error en el sistema");
//				e.printStackTrace();
//				
//			}
//		}else{
//			respuestaHistorialDTO.setCodigo(10);
//			respuestaHistorialDTO.setMensaje(resu.getMensaje());	
//		}
//			
//		return respuestaHistorialDTO;
//	}

	
}
