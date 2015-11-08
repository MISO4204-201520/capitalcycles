package com.sofactory.negocio;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.dtos.RespuestaMensajeDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Mensaje;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.MensajeBeanLocal;

@Stateless
@Local({MensajeBeanLocal.class})
public class NotificacionBean extends GenericoBean<Mensaje> implements MensajeBeanLocal{

	@PersistenceContext(unitName="MensajesPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public List<Mensaje> mensajesEnviadosPorUsuario(long codUser){return null;}
	
	public List<Mensaje> mensajesRecibidosPorUsuario(long codUser){return null;}

	@Override
	public RespuestaMensajeDTO enviarMensaje(UsuarioDTO usrdesde, UsuarioDTO usrpara, String texto) {
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO();
		respuestaMensajeDTO.setCodigo(0);
		respuestaMensajeDTO.setMensaje("OK");
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
														InternetAddress.parse(usrpara.getCorreo()));
			message.setSubject("Mensaje desde : "+ usrdesde.getCorreo());
			message.setText(texto);
			Transport.send(message);
			
		} catch (MessagingException e) {
			respuestaMensajeDTO.setCodigo(2);
			respuestaMensajeDTO.setMensaje("Error en el Envio del Email");
		}
		
		return respuestaMensajeDTO;
	}
	
}