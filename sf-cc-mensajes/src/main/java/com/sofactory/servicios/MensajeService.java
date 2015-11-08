package com.sofactory.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sofactory.dtos.AmigoDTO;
import com.sofactory.dtos.MensajeDTO;
import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaAmigoDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.dtos.RespuestaMensajeDTO;
import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Amigo;
import com.sofactory.entidades.Mensaje;
import com.sofactory.negocio.interfaces.AmigoBeanLocal;
import com.sofactory.negocio.interfaces.MensajeBeanLocal;

import play.libs.Json;

@Path("mensajeService")
public class MensajeService {

	private static String servicioGetEncontrarUsuario = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarUsuarioPorCodigo/";
	private static String servicioObtenerUsuarioSesion = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/obtenerUsuarioSesion";
	private static String servicioRegistrarServicio = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/registrarServicio";
	private String getUsuarioPorCodigo = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarUsuarioPorCodigo/";
	private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	@EJB(beanName="MensajeBean")
	private MensajeBeanLocal mensajeBeanLocal;
	
	@EJB(beanName="NotificacionBean")
	private MensajeBeanLocal notificacionBeanLocal;
	
	@EJB
	private AmigoBeanLocal amigoBeanLocal;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("obtenerStatus")
	public RespuestaMensajeDTO getStatus() {
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(new Long(1));
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resu.getCodigo());
		System.out.println("RESU "+resu.getMensaje());
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "{\"status \":\"sf-cc-mensajes is running...\"}");
			
		return respuestaMensajeDTO;
	
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
													   m.getTexto(),m.getStatus(), FORMATO_FECHA.format(m.getFecha()));
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
	@Path("encontrarMensajePorId/{cod}/{id}")
	public RespuestaMensajeDTO encontrarMensajePorId(@PathParam("cod") long codUsuario, @PathParam("id") long idMensaje) {
	
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(codUsuario);
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resu.getCodigo());
		System.out.println("RESU "+resu.getMensaje());
		
		if (resu.getCodigo()==0){
		
				try {
					
					Mensaje m = mensajeBeanLocal.encontrarPorId(Mensaje.class, new Long(idMensaje));	
		
					if (m!=null) {
						MensajeDTO mensajeDTO = new MensajeDTO(m.getId(), m.getUsrdesde(), m.getUsrpara(),
								   m.getTexto(),m.getStatus(), FORMATO_FECHA.format(m.getFecha()));
						respuestaMensajeDTO.getMensajes().add(mensajeDTO);
						
						// Inicio Otorga Puntos por Fidelizacion
						
						RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
						registrarPuntosDTO.setCodigoUsuario(Long.toString(codUsuario));
						registrarPuntosDTO.setServicio("encontrarMensajePorId");
					
						client = ClientBuilder.newClient();
						targetMensaje = client.target(servicioRegistrarServicio);
						RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);		
						
						// Fin Otorga Puntos por Fidelizacion
						
					}else{
						respuestaMensajeDTO.setCodigo(2);
						respuestaMensajeDTO.setMensaje("El Mensaje No existe en el sistema");
					}
					
				}catch(Exception e){
					respuestaMensajeDTO.setCodigo(1);
					respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
					e.printStackTrace();
				}
		}else{
			
			respuestaMensajeDTO.setCodigo(10);
			respuestaMensajeDTO.setMensaje(resu.getMensaje());
			
		}
		
		return respuestaMensajeDTO;

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("mensajesEnviadosPorUsuario/{usuario}")
	public RespuestaMensajeDTO mensajesEnviadosPorUsuario(@PathParam("usuario") long usuario) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(usuario);
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resu.getCodigo());
		System.out.println("RESU "+resu.getMensaje());
		
		if (resu.getCodigo()==0){
		
			try {
	
				List<Mensaje> mensajes = mensajeBeanLocal.mensajesEnviadosPorUsuario(usuario);
				
				for  (Mensaje m: mensajes){
					MensajeDTO mensajeDTO = new MensajeDTO(m.getId(), m.getUsrdesde(), m.getUsrpara(),
														   m.getTexto(),m.getStatus(),FORMATO_FECHA.format(m.getFecha()));
					respuestaMensajeDTO.getMensajes().add(mensajeDTO);
				}
				
				if (mensajes.size()>0){
	
					// Inicio Otorga Puntos por Fidelizacion		
					
					RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
					registrarPuntosDTO.setCodigoUsuario(Long.toString(usuario));
					registrarPuntosDTO.setServicio("mensajesEnviadosPorUsuario");
				
					client = ClientBuilder.newClient();
					targetMensaje = client.target(servicioRegistrarServicio);
					RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);			
				
					// Fin Otorga Puntos por Fidelizacion				

				}
			} catch (Exception e) {
	
				respuestaMensajeDTO.setCodigo(1);
				respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
				e.printStackTrace();
				
			}
		}else{
			respuestaMensajeDTO.setCodigo(10);
			respuestaMensajeDTO.setMensaje(resu.getMensaje());	
		}
			
		return respuestaMensajeDTO;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("mensajesRecibidosPorUsuario/{usuario}")
	public RespuestaMensajeDTO mensajesRecibidosPorUsuario(@PathParam("usuario") long usuario) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(usuario);
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resu.getCodigo());
		System.out.println("RESU "+resu.getMensaje());
		
		if (resu.getCodigo()==0){
		
			try {
	
				List<Mensaje> mensajes = mensajeBeanLocal.mensajesRecibidosPorUsuario(usuario);
				
				for  (Mensaje m: mensajes){
					MensajeDTO mensajeDTO = new MensajeDTO(m.getId(), m.getUsrdesde(), m.getUsrpara(),
														   m.getTexto(),m.getStatus(), FORMATO_FECHA.format(m.getFecha()));
					respuestaMensajeDTO.getMensajes().add(mensajeDTO);
				}			
				
				if (mensajes.size()>0){
				
					// Inicio Otorga Puntos por Fidelizacion		
					
					RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
					registrarPuntosDTO.setCodigoUsuario(Long.toString(usuario));
					registrarPuntosDTO.setServicio("mensajesRecibidosPorUsuario");
				
					client = ClientBuilder.newClient();
					targetMensaje = client.target(servicioRegistrarServicio);
					RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);			
				
					// Fin Otorga Puntos por Fidelizacion				
				}
				
			} catch (Exception e) {
				
				respuestaMensajeDTO.setCodigo(1);
				respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
				e.printStackTrace();
			
			}
		}else{
			respuestaMensajeDTO.setCodigo(10);
			respuestaMensajeDTO.setMensaje(resu.getMensaje());	
		}
			
		return respuestaMensajeDTO;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("crearNuevoMensaje")
	public RespuestaMensajeDTO crearNuevoMensaje(MensajeDTO mDTO) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		respuestaMensajeDTO.setCodigoUsuario(mDTO.getUsrdesde().toString());

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(mDTO.getUsrdesde());
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resuSeg = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resuSeg.getCodigo());
		System.out.println("RESU "+resuSeg.getMensaje());
		
		if (resuSeg.getCodigo()==0){
			
			if (mDTO.getUsrdesde()!=null && mDTO.getUsrpara()!=null && mDTO.getTexto()!=null){
				try {
					client = ClientBuilder.newClient();
					targetMensaje = client.target(servicioGetEncontrarUsuario+mDTO.getUsrdesde());
					RespuestaUsuarioDTO resuDesde = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
					if (resuDesde!=null && resuDesde.getCodigo()==0){
						targetMensaje = client.target(servicioGetEncontrarUsuario+mDTO.getUsrpara());
						RespuestaUsuarioDTO resuPara = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
						if (resuPara!=null && resuPara.getCodigo()==0){
							respuestaMensajeDTO = mensajeBeanLocal.enviarMensaje(resuDesde.getUsuarios().get(0), resuPara.getUsuarios().get(0), mDTO.getTexto());
							// Inicio Otorga Puntos por Fidelizacion
							RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
							registrarPuntosDTO.setCodigoUsuario(usuarioDTO.getCodigo().toString());
							registrarPuntosDTO.setServicio("crearNuevoMensaje");
						
							client = ClientBuilder.newClient();
							targetMensaje = client.target(servicioRegistrarServicio);
							RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);		
							
							// Fin Otorga Puntos por Fidelizacion
						}else{			
							respuestaMensajeDTO.setCodigo(3);
							respuestaMensajeDTO.setMensaje("Usuario Destinatario No Existe ... ");
						}
						
					}else{
						respuestaMensajeDTO.setCodigo(2);
						respuestaMensajeDTO.setMensaje("Usuario Remitente No Existe ... ");
					}
					
				} catch (Exception e) {
					respuestaMensajeDTO.setCodigo(1);
					respuestaMensajeDTO.setMensaje("Faltan Campos Obligatorios");
				}
			}
		
		}else{
			respuestaMensajeDTO.setCodigo(10);
			respuestaMensajeDTO.setMensaje(resuSeg.getMensaje());	
		}		
			
		return respuestaMensajeDTO;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("actualizarMensaje")
	public RespuestaMensajeDTO actualizarMensaje(MensajeDTO mensajeDTO) {

		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(mensajeDTO.getUsrdesde());
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resuSeg = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU desde "+resuSeg.getCodigo());
		System.out.println("RESU desde "+resuSeg.getMensaje());
		
		boolean estaUsuarioSesion = false;
		if (resuSeg.getCodigo()==0){
			estaUsuarioSesion=true;
		}else{
			usuarioDTO.setCodigo(mensajeDTO.getUsrpara());
			client = ClientBuilder.newClient();
			targetMensaje = client.target(servicioObtenerUsuarioSesion);
			resuSeg = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
			if (resuSeg.getCodigo()==0){
				estaUsuarioSesion=true;
			}
			System.out.println("RESU para"+resuSeg.getCodigo());
			System.out.println("RESU para"+resuSeg.getMensaje());		
		}
		
		if (estaUsuarioSesion){
		
			try {
	
				//Validar si el mensaje existe en el sistema
				Mensaje mensajeActualizar = mensajeBeanLocal.encontrarPorId(Mensaje.class, mensajeDTO.getId());
				
				if (mensajeActualizar!=null){
					if (mensajeActualizar.getStatus()==true){
						respuestaMensajeDTO.setCodigo(1);
						respuestaMensajeDTO.setMensaje("El Mensaje con Id : "+mensajeActualizar.getId()+ 
												                                     		" ya tiene estatus Leido");
					}else{
						
						if (mensajeActualizar.getUsrdesde()==mensajeDTO.getUsrdesde()){
							mensajeActualizar.setStatus(true);
							mensajeBeanLocal.insertarOActualizar(mensajeActualizar);
							
							// Inicio Otorga Puntos por Fidelizacion
							
							RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
							registrarPuntosDTO.setCodigoUsuario(usuarioDTO.getCodigo().toString());
							registrarPuntosDTO.setServicio("actualizarMensaje");
						
							client = ClientBuilder.newClient();
							targetMensaje = client.target(servicioRegistrarServicio);
							RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);		
							
							// Fin Otorga Puntos por Fidelizacion
							
						}else{
							respuestaMensajeDTO.setCodigo(4);
							respuestaMensajeDTO.setMensaje("El Mensaje con Id : "+mensajeActualizar.getId()+ 
												            " No pertence al Usuario " + mensajeDTO.getUsrdesde());
						}
					}
				}else{
					respuestaMensajeDTO.setCodigo(2);
					respuestaMensajeDTO.setMensaje("El Mensaje no existe en el sistema");
				}
				
			} catch (Exception err) {
				
				respuestaMensajeDTO.setCodigo(3);
				respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");
				
			}
		}else{
			respuestaMensajeDTO.setCodigo(10);
			respuestaMensajeDTO.setMensaje(resuSeg.getMensaje());	
		}	
		
		return respuestaMensajeDTO;
	
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removerMensaje")
	public RespuestaMensajeDTO removerMensaje(MensajeDTO mensajeDTO) {

		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
	
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(mensajeDTO.getUsrdesde());
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resuSeg = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resuSeg.getCodigo());
		System.out.println("RESU "+resuSeg.getMensaje());
		
		if (resuSeg.getCodigo()==0){
		
			try {
			
				//Validar si el mensaje existe en el sistema
				Mensaje mensajeRemover = mensajeBeanLocal.encontrarPorId(Mensaje.class, mensajeDTO.getId());
				
				if (mensajeRemover!=null){
				
					if (mensajeRemover.getUsrdesde()==mensajeDTO.getUsrdesde()){
						mensajeBeanLocal.remover(mensajeRemover, mensajeRemover.getId());
					}else{
						respuestaMensajeDTO.setCodigo(4);
						respuestaMensajeDTO.setMensaje("El Mensaje con Id : "+mensajeRemover.getId()+ 
											            " No pertence al Usuario " + mensajeDTO.getUsrdesde());
					}
						
				}else{
					respuestaMensajeDTO.setCodigo(1);
					respuestaMensajeDTO.setMensaje("El Mensaje no existe en el sistema");
				}
				
			} catch (Exception e) {
				
				respuestaMensajeDTO.setCodigo(2);
				respuestaMensajeDTO.setMensaje("Hubo un error en el sistema");		
			}
		
		}else{
			respuestaMensajeDTO.setCodigo(10);
			respuestaMensajeDTO.setMensaje(resuSeg.getMensaje());	
		}	
		
		return respuestaMensajeDTO;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("enviarCorreo")
	public RespuestaMensajeDTO enviarCorreo(MensajeDTO mDTO) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
	
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(mDTO.getUsrdesde());
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resuSeg = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resuSeg.getCodigo());
		System.out.println("RESU "+resuSeg.getMensaje());
		
		if (resuSeg.getCodigo()==0){
		
			if (mDTO.getUsrdesde()!=null && mDTO.getUsrpara()!=null && mDTO.getTexto()!=null){
				try {
					client = ClientBuilder.newClient();
					targetMensaje = client.target(servicioGetEncontrarUsuario+mDTO.getUsrdesde());
					RespuestaUsuarioDTO resuDesde = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
					if (resuDesde!=null && resuDesde.getCodigo()==0){
						try {
							targetMensaje = client.target(servicioGetEncontrarUsuario+mDTO.getUsrpara());
							RespuestaUsuarioDTO resuPara = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
							if (resuPara!=null && resuPara.getCodigo()==0){
		
								//Envio email segun dirección de correo usuario	destino			
								respuestaMensajeDTO = notificacionBeanLocal.enviarMensaje(resuDesde.getUsuarios().get(0),resuPara.getUsuarios().get(0),mDTO.getTexto());
								
								// Inicio Otorga Puntos por Fidelizacion
								
								RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
								registrarPuntosDTO.setCodigoUsuario(usuarioDTO.getCodigo().toString());
								registrarPuntosDTO.setServicio("enviarCorreo");
							
								client = ClientBuilder.newClient();
								targetMensaje = client.target(servicioRegistrarServicio);
								RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);		
								
								// Fin Otorga Puntos por Fidelizacion
							}else{
								respuestaMensajeDTO.setCodigo(3);
								respuestaMensajeDTO.setMensaje("Usuario Destinatario No Existe");
							}	
								
						}catch (Exception e ) {
			//							throw new RuntimeException(e);
										respuestaMensajeDTO.setCodigo(4);
										respuestaMensajeDTO.setMensaje("Error Interno del Sistema");
						}
						
					}else{
						respuestaMensajeDTO.setCodigo(5);
						respuestaMensajeDTO.setMensaje("Usuario Remitente No Existe");
					}
	
				}catch (Exception e) {
					//			throw new RuntimeException(e);
								respuestaMensajeDTO.setCodigo(4);
								respuestaMensajeDTO.setMensaje("Error Interno del Sistema");
				}
					
					
			}else{
					respuestaMensajeDTO.setCodigo(1);
					respuestaMensajeDTO.setMensaje("Faltan Campos Obligatorios");
			}
		
		}else{
			
			respuestaMensajeDTO.setCodigo(10);
			respuestaMensajeDTO.setMensaje(resuSeg.getMensaje());	
		}	
			
		return respuestaMensajeDTO;
	
	}
	
	//********* Amigos

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("crearAmigo")
	public RespuestaAmigoDTO crearAmigo(AmigoDTO aDTO) {
		
		RespuestaAmigoDTO respuestaAmigoDTO = new RespuestaAmigoDTO(0, "OK");
		respuestaAmigoDTO.setCodigoUsuario(aDTO.getCodUsuario().toString());

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(aDTO.getCodUsuario());
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resuSeg = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resuSeg.getCodigo());
		System.out.println("RESU "+resuSeg.getMensaje());
		
		if (resuSeg.getCodigo()==0){
			
			if ((aDTO.getCodUsuario()!=null && aDTO.getCodAmigo()!=null) && (aDTO.getCodUsuario()!=aDTO.getCodAmigo())){
					
				try {				
					
					client = ClientBuilder.newClient();
					targetMensaje = client.target(servicioGetEncontrarUsuario+aDTO.getCodUsuario());
					RespuestaUsuarioDTO resuCodUsuario = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
				
					if (resuCodUsuario!=null && resuCodUsuario.getCodigo()==0){
					
						targetMensaje = client.target(servicioGetEncontrarUsuario+aDTO.getCodAmigo());
						RespuestaUsuarioDTO resuCodAmigo = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
						
						if (resuCodAmigo!=null && resuCodAmigo.getCodigo()==0){

							boolean yaAmigo = amigoBeanLocal.amigoDeUsuario(aDTO.getCodUsuario(), aDTO.getCodAmigo());
							
							if (!yaAmigo){
							
								Amigo amigo = new Amigo();
						
								amigo.setCodUsuario(aDTO.getCodUsuario());
								amigo.setCodAmigo(aDTO.getCodAmigo());
						
								amigoBeanLocal.insertar(amigo);
	
								// Inicio Otorga Puntos por Fidelizacion
								
								RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
								registrarPuntosDTO.setCodigoUsuario(usuarioDTO.getCodigo().toString());
								registrarPuntosDTO.setServicio("crearAmigo");
							
								client = ClientBuilder.newClient();
								targetMensaje = client.target(servicioRegistrarServicio);
								RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);		
								
								// Fin Otorga Puntos por Fidelizacion
							
							}else{			
								respuestaAmigoDTO.setCodigo(5);
								respuestaAmigoDTO.setMensaje("Codigo de Amigo Ya Existe ... ");
							}	
							
						}else{			
							respuestaAmigoDTO.setCodigo(3);
							respuestaAmigoDTO.setMensaje("Codigo de Amigo No Existe ... ");
						}
						
					}else{
						respuestaAmigoDTO.setCodigo(2);
						respuestaAmigoDTO.setMensaje("Codigo de Usuario No Existe ... ");
					}
					
				} catch (Exception e) {
					respuestaAmigoDTO.setCodigo(4);
					respuestaAmigoDTO.setMensaje("Error Interno del Sistema ...");
				}
			
			}else{
				respuestaAmigoDTO.setCodigo(1);
				respuestaAmigoDTO.setMensaje("Faltan Campos Obligatorios o Codigo de Usuario igual a Codigo de amigo");
			}
		
		}else{
			respuestaAmigoDTO.setCodigo(10);
			respuestaAmigoDTO.setMensaje(resuSeg.getMensaje());	
		}		
			
		return respuestaAmigoDTO;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("amigosDeUsuario/{usuario}")
	public RespuestaAmigoDTO amigosDeUsuario(@PathParam("usuario") long usuario) {
		
		RespuestaAmigoDTO respuestaAmigoDTO = new RespuestaAmigoDTO(0, "OK");
		respuestaAmigoDTO.setCodigoUsuario(Long.toString(usuario));
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(usuario);
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resu.getCodigo());
		System.out.println("RESU "+resu.getMensaje());
		
		if (resu.getCodigo()==0){
		
			try {
	
				List<Amigo> Amigos = amigoBeanLocal.amigosDeUsuario(usuario);
				client = ClientBuilder.newClient();
				targetMensaje = client.target(getUsuarioPorCodigo);
				for  (Amigo a: Amigos){
					AmigoDTO amigoDTO = new AmigoDTO(a.getId(), a.getCodUsuario(), a.getCodAmigo());
					targetMensaje = client.target(getUsuarioPorCodigo+a.getCodAmigo());
					RespuestaUsuarioDTO respuesta = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
					if (respuesta!=null){
						if (respuesta.getCodigo()==0){
							usuarioDTO = respuesta.getUsuarios().get(0);
							amigoDTO.setNombres(usuarioDTO.getNombres());
							amigoDTO.setApellidos(usuarioDTO.getApellidos());
							amigoDTO.setCorreo(usuarioDTO.getCorreo());
							amigoDTO.setFoto(usuarioDTO.getFoto());
						}
					}
					respuestaAmigoDTO.getAmigos().add(amigoDTO);
				}
				
			} catch (Exception e) {
	
				respuestaAmigoDTO.setCodigo(1);
				respuestaAmigoDTO.setMensaje("Hubo un error en el sistema");
				e.printStackTrace();
				
			}
		}else{
			respuestaAmigoDTO.setCodigo(10);
			respuestaAmigoDTO.setMensaje(resu.getMensaje());	
		}
			
		return respuestaAmigoDTO;
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removerAmigo/{id}/{codUsuario}/{codAmigo}")
	public RespuestaAmigoDTO removerAmigo(@PathParam("id") Long id, @PathParam("codUsuario") Long codUsuario, @PathParam("codAmigo") Long codAmigo) {

		RespuestaAmigoDTO respuestaAmigoDTO = new RespuestaAmigoDTO(0, "OK");
		respuestaAmigoDTO.setCodigoUsuario(Long.toString(codUsuario));
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(codUsuario);
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resuSeg = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		System.out.println("RESU "+resuSeg.getCodigo());
		System.out.println("RESU "+resuSeg.getMensaje());
		
		if (resuSeg.getCodigo()==0){
		
			try {
			
				//Validar si el amigo existe en el sistema
				Amigo amigoRemover = amigoBeanLocal.encontrarPorId(Amigo.class, id);
						
				if (amigoRemover!=null){
				
					if (amigoRemover.getCodUsuario().longValue()==codUsuario.longValue() && 
						amigoRemover.getCodAmigo().longValue()==codAmigo.longValue()) {
				
						amigoBeanLocal.remover(amigoRemover, amigoRemover.getId());
					
					}else{
					
						respuestaAmigoDTO.setCodigo(4);
						respuestaAmigoDTO.setMensaje("El Amigo : "+codAmigo+ 
											            " No pertence al Usuario " + codUsuario);
					}
						
				}else{
					System.out.println(amigoRemover);
					respuestaAmigoDTO.setCodigo(1);
					respuestaAmigoDTO.setMensaje("El Amigo con Id:  "+id +" no existe en el sistema");
				}
				
			} catch (Exception e) {
				
				respuestaAmigoDTO.setCodigo(2);
				respuestaAmigoDTO.setMensaje("Hubo un error en el sistema");		
			}
		
		}else{
			respuestaAmigoDTO.setCodigo(10);
			respuestaAmigoDTO.setMensaje(resuSeg.getMensaje());	
		}	
		
		return respuestaAmigoDTO;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("encontrarUsuariosNoAmigos/{usuario}/{completar}")
	public RespuestaAmigoDTO encontrarUsuariosNoAmigos(@PathParam("usuario") Long usuario, @PathParam("completar") String completar) {
		
		RespuestaAmigoDTO respuestaAmigoDTO = new RespuestaAmigoDTO(0, "OK");
		respuestaAmigoDTO.setCodigoUsuario(Long.toString(usuario));
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(usuario);
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		if (resu.getCodigo()==0){
			try {
				List<UsuarioDTO> usuarios = amigoBeanLocal.encontrarUsuariosNoAmigos(completar, usuario, 20);
				respuestaAmigoDTO.setUsuarioDTOs(usuarios);
			} catch (Exception e) {
	
				respuestaAmigoDTO.setCodigo(1);
				respuestaAmigoDTO.setMensaje("Hubo un error en el sistema");
				e.printStackTrace();
				
			}
		}else{
			respuestaAmigoDTO.setCodigo(10);
			respuestaAmigoDTO.setMensaje(resu.getMensaje());	
		}
			
		return respuestaAmigoDTO;
	}
}