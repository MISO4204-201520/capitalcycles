package com.sofactory.servicios;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sofactory.dtos.RespuestaMensajeDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.MensajeDTO;
import com.sofactory.entidades.Mensaje;
import com.sofactory.negocio.interfaces.MensajeBeanLocal;


@Path("mensajeService")
public class MensajeService {

	private static String servicioGetEncontrarUsuario = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarUsuarioPorCodigo/";
	
	@EJB
	private MensajeBeanLocal mensajeBeanLocal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("obtenerStatus")
	public Response getStatus() {
		return Response.ok("{\"status \":\"sf-cc-mensajes is running...\"}").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("obtenerTodosMensajes")
	public RespuestaMensajeDTO obtenerTodosMensajes() {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		try {
		
			List<Mensaje> mensajes = mensajeBeanLocal.encontrarTodos(Mensaje.class, "id", "ASC");
			
			for  (Mensaje m: mensajes){
				MensajeDTO mensajeDTO = new MensajeDTO(m.getId(), m.getUsrdesde(), m.getUsrdesde(),
													   m.getTexto(),m.getStatus(), m.getFecha());
				respuestaMensajeDTO.getMensajes().add(mensajeDTO);
			}
			
		} catch (Exception e) {
			respuestaMensajeDTO.setCodigo(1);
			respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		return respuestaMensajeDTO;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("encontrarMensajePorId/{id}")
	public RespuestaMensajeDTO encontrarMensajePorId(@PathParam("id") long idMensaje) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		try {
			
			Mensaje m = mensajeBeanLocal.encontrarPorId(Mensaje.class, new Long(idMensaje));	

			if (m!=null) {
				MensajeDTO mensajeDTO = new MensajeDTO(m.getId(), m.getUsrdesde(), m.getUsrpara(),
						   m.getTexto(),m.getStatus(), m.getFecha());
				respuestaMensajeDTO.getMensajes().add(mensajeDTO);
			}else{
				respuestaMensajeDTO.setCodigo(2);
				respuestaMensajeDTO.setMensaje("El Mensaje No existe en el sistema");
			}
			
		}catch(Exception e){
			respuestaMensajeDTO.setCodigo(1);
			respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		
		return respuestaMensajeDTO;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("mensajesEnviadosPorUsuario/{usuario}")
	public RespuestaMensajeDTO mensajesEnviadosPorUsuario(@PathParam("usuario") long usuario) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		try {

			List<Mensaje> mensajes = mensajeBeanLocal.mensajesEnviadosPorUsuario(usuario);
			
			for  (Mensaje m: mensajes){
				MensajeDTO mensajeDTO = new MensajeDTO(m.getId(), m.getUsrdesde(), m.getUsrpara(),
													   m.getTexto(),m.getStatus(), m.getFecha());
				respuestaMensajeDTO.getMensajes().add(mensajeDTO);
			}

		} catch (Exception e) {

			respuestaMensajeDTO.setCodigo(1);
			respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
			
		}
		return respuestaMensajeDTO;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("mensajesRecibidosPorUsuario/{usuario}")
	public RespuestaMensajeDTO mensajesRecibidosPorUsuario(@PathParam("usuario") long usuario) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		try {

			List<Mensaje> mensajes = mensajeBeanLocal.mensajesRecibidosPorUsuario(usuario);
			
			for  (Mensaje m: mensajes){
				MensajeDTO mensajeDTO = new MensajeDTO(m.getId(), m.getUsrdesde(), m.getUsrpara(),
													   m.getTexto(),m.getStatus(), m.getFecha());
				respuestaMensajeDTO.getMensajes().add(mensajeDTO);
			}			
			
		} catch (Exception e) {
			
			respuestaMensajeDTO.setCodigo(1);
			respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		
		}
		
		return respuestaMensajeDTO;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("crearNuevoMensaje")
	public RespuestaMensajeDTO crearNuevoMensaje(MensajeDTO mDTO) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		
		if (mDTO.getUsrdesde()!=null && mDTO.getUsrpara()!=null && mDTO.getTexto()!=null){

			mDTO.setFecha(new Date());
			mDTO.setStatus(false);
			
			try {
//				Client client = ClientBuilder.newClient();
//				WebTarget targetMensaje = client.target(servicioGetEncontrarUsuario+mDTO.getUsrdesde());
//				RespuestaUsuarioDTO resu = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
			
//				if (resu!=null && resu.getCodigo()==0){
				
//					targetMensaje = client.target(servicioGetEncontrarUsuario+mDTO.getUsrpara());
//					resu = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
					
//					if (resu!=null && resu.getCodigo()==0){
					
						Mensaje mensaje = new Mensaje();
				
						mensaje.setUsrdesde(mDTO.getUsrdesde());
						mensaje.setUsrpara(mDTO.getUsrpara());
						mensaje.setTexto(mDTO.getTexto());
						mensaje.setStatus(mDTO.getStatus());
						mensaje.setFecha(mDTO.getFecha());
				
						mensajeBeanLocal.insertar(mensaje);
				
//					}else{			
//						respuestaMensajeDTO.setCodigo(3);
//						respuestaMensajeDTO.setMensaje("Usuario Destinatario No Existe ... ");
//					}
					
//				}else{
//					respuestaMensajeDTO.setCodigo(2);
//					respuestaMensajeDTO.setMensaje("Usuario Remitente No Existe ... ");
//				}
				
			} catch (Exception e) {
				respuestaMensajeDTO.setCodigo(1);
				respuestaMensajeDTO.setMensaje("Faltan Campos Obligatorios");
			}
		
		}
		return respuestaMensajeDTO;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("actualizarMensaje")
	public RespuestaMensajeDTO actualizarMensaje(MensajeDTO mensajeDTO) {

		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");

		try {

			//Validar si el mensaje existe en el sistema
			Mensaje mensajeActualizar = mensajeBeanLocal.encontrarPorId(Mensaje.class, mensajeDTO.getId());
			
			if (mensajeActualizar!=null){
				if (mensajeActualizar.getStatus()==true){
					respuestaMensajeDTO.setCodigo(1);
					respuestaMensajeDTO.setMensaje("El Mensaje con Id : "+mensajeActualizar.getId()+ 
											                                     		" ya tiene estatus Leido");
				}else{
					
					mensajeActualizar.setStatus(true);
					mensajeBeanLocal.insertarOActualizar(mensajeActualizar);
				}
			}else{
				respuestaMensajeDTO.setCodigo(2);
				respuestaMensajeDTO.setMensaje("El Mensaje no existe en el sistema");
			}
			
		} catch (Exception err) {
			
			respuestaMensajeDTO.setCodigo(3);
			respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
			
		}
		
		return respuestaMensajeDTO;
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removerMensaje")
	public RespuestaMensajeDTO removerMensaje(MensajeDTO mensajeDTO) {

		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		
		try {
		
			//Validar si el mensaje existe en el sistema
			Mensaje mensajeRemover = mensajeBeanLocal.encontrarPorId(Mensaje.class, mensajeDTO.getId());
			
			if (mensajeRemover!=null){
			
				mensajeBeanLocal.remover(mensajeRemover, mensajeRemover.getId());
				
			}else{
				respuestaMensajeDTO.setCodigo(1);
				respuestaMensajeDTO.setMensaje("El Mensaje no existe en el sistema");
			}
			
		} catch (Exception e) {
			
			respuestaMensajeDTO.setCodigo(2);
			respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
			
		}
		return respuestaMensajeDTO;
	}

}