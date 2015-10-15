package com.sofactory.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sofactory.dtos.RespuestaMensajeDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sofactory.dtos.MensajeDTO;
import com.sofactory.entidades.Mensaje;
import com.sofactory.negocio.interfaces.MensajeBeanLocal;

import play.libs.Json;

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
				
				
				
				
				Client client = ClientBuilder.newClient();
				WebTarget targetMensaje = client.target(servicioGetEncontrarUsuario+mDTO.getUsrdesde());
				RespuestaUsuarioDTO resu = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
			
				if (resu!=null && resu.getCodigo()==0){
				
					targetMensaje = client.target(servicioGetEncontrarUsuario+mDTO.getUsrpara());
					resu = targetMensaje.request("application/json").get(RespuestaUsuarioDTO.class);
					
					if (resu!=null && resu.getCodigo()==0){
					
						Mensaje mensaje = new Mensaje();
				
						mensaje.setUsrdesde(mDTO.getUsrdesde());
						mensaje.setUsrpara(mDTO.getUsrpara());
						mensaje.setTexto(mDTO.getTexto());
						mensaje.setStatus(mDTO.getStatus());
						mensaje.setFecha(mDTO.getFecha());
				
						mensajeBeanLocal.insertar(mensaje);


						//Envio mensaje txt al Celular segun token del usuario				
						boolean msgtxt=false;
						
						if (msgtxt==true)
						{
							String apiKey = "LUIS";
							String TOKEN="Token";
						
							try {
								// Prepare JSON containing the GCM message content. What to send and where to send.
								ObjectNode jGcmData = Json.newObject();
								ObjectNode jData = Json.newObject();
								jData.put("message", mensaje.getTexto());
								// Where to send GCM message.
								jGcmData.put("to", TOKEN);
								// What to send in GCM message.
								jGcmData.put("data", jData);

								// Create connection to send GCM Message request.
								URL url = new URL("https://android.googleapis.com/gcm/send");
								HttpURLConnection conn = (HttpURLConnection) url.openConnection();
								conn.setRequestProperty("Authorization", "key=" + apiKey);
								conn.setRequestProperty("Content-Type", "application/json");
								conn.setRequestMethod("POST");
								conn.setDoOutput(true);

								// Send GCM message content.
								OutputStream outputStream = conn.getOutputStream();
								outputStream.write(jGcmData.toString().getBytes());

								// Read GCM response.
								InputStream inputStream = conn.getInputStream();
								String resp = getStringFromInputStream(inputStream);
								System.out.println(resp);
								System.out.println("Check your device/emulator for notification or logcat for " +
										"confirmation of the receipt of the GCM message.");
							} catch (IOException e) {
								System.out.println("Unable to send GCM message.");
								System.out.println("Please ensure that API_KEY has been replaced by the server " +
										"API key, and that the device's registration token is correct (if specified).");
								e.printStackTrace();
							}
							
						}
						
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
		
		return respuestaMensajeDTO;
	}

	private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return sb.toString();
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
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("enviarCorreo")
	public RespuestaMensajeDTO enviarCorreo(MensajeDTO mDTO) {
		
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO(0, "OK");
		
		if (mDTO.getUsrdesde()!=null && mDTO.getUsrpara()!=null && mDTO.getTexto()!=null){

			mDTO.setFecha(new Date());
			mDTO.setStatus(false);
							
			Mensaje mensaje = new Mensaje();
			
			mensaje.setUsrdesde(mDTO.getUsrdesde());
			mensaje.setUsrpara(mDTO.getUsrpara());
			mensaje.setTexto(mDTO.getTexto());
			mensaje.setStatus(mDTO.getStatus());
			mensaje.setFecha(mDTO.getFecha());

			//Envio email segun dirección de correo usuario				

			final String username = "capytalcycles@gmail.com";
			final String password = "capytal2015";

			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					}
			);
						
			try {
	
				System.out.println("... Sending Mail ...");					
						
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("capytalcycles@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("fergutierrez@yahoo.com"));
				message.setSubject("Notificacion ");
				message.setText("Pruebas"
					            		+ "\n\n .... Pruebas .....");
										
				Transport.send(message);
	
			} catch (MessagingException e) {
									throw new RuntimeException(e);
			}
			
		}else{
				respuestaMensajeDTO.setCodigo(1);
				respuestaMensajeDTO.setMensaje("Faltan Campos Obligatorios");
		}
	
		return respuestaMensajeDTO;
	
	}
}