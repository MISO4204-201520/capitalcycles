package com.sofactory.negocio;

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

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sofactory.dtos.RespuestaMensajeDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Mensaje;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.MensajeBeanLocal;

import play.libs.Json;

@Stateless
@Local({MensajeBeanLocal.class})
public class MensajeBean extends GenericoBean<Mensaje> implements MensajeBeanLocal{

	private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@PersistenceContext(unitName="MensajesPU")
	private EntityManager em;

	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}

	public List<Mensaje> mensajesEnviadosPorUsuario(long codUser){

		Query query = em.createQuery("FROM com.sofactory.entidades.Mensaje WHERE USRDESDE=:codUser ORDER BY FECHA")
				.setParameter("codUser", codUser);
		List<Mensaje> list = query.getResultList();

		return list;

	}

	public List<Mensaje> mensajesRecibidosPorUsuario(long codUser){

		Query query = em.createQuery("FROM com.sofactory.entidades.Mensaje WHERE USRPARA=:codUser ORDER BY FECHA")
				.setParameter("codUser", codUser);
		List<Mensaje> list = query.getResultList();

		return list;
	}

	@Override
	public RespuestaMensajeDTO enviarMensaje(UsuarioDTO usrdesde, UsuarioDTO usrpara, String texto) {
		RespuestaMensajeDTO respuestaMensajeDTO = new RespuestaMensajeDTO();
		respuestaMensajeDTO.setCodigo(0);
		respuestaMensajeDTO.setMensaje("OK");
		
		Mensaje mensaje = new Mensaje();
		mensaje.setUsrdesde(usrdesde.getCodigo());
		mensaje.setUsrpara(usrpara.getCodigo());
		mensaje.setTexto(texto);
		mensaje.setStatus(false);
		mensaje.setFecha(new Date());
		insertarOActualizar(mensaje);
		
		//Envio mensaje txt al Celular segun token del usuario
		String token = usrpara.getToken();
		System.out.println("TOKEN USUARIO PARA "+token);
		if (token!="SI")
		{
			String apiKey = "AIzaSyAUMsAY_oY3IgmtEyDVqXyLrZQxYbnMM_k";
			//	TOKEN="elUpDaFOJjQ:APA91bEnkQCsE_A7LD-6qFIAbi3ixEMYw79rRqRgvUJKfrdY_bzGbIhJnQEA2wRkoqJ7FrTSh_qmech0y2JvVxo4t-J62Kje2ilugwcFAZDzS3eJZsWjRTtyeqIy7sqb51EqUS3y_TSc";

			try {
				// Prepare JSON containing the GCM message content. What to send and where to send.
				ObjectNode jGcmData = Json.newObject();
				ObjectNode jData = Json.newObject();
				jData.put("message", mensaje.getTexto());
				jData.put("senderId", mensaje.getUsrdesde());
				jData.put("senderName", usrdesde.getNombres()+" "+usrdesde.getApellidos());
				// Where to send GCM message.
				jGcmData.put("to", token);
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
			} catch (Exception e) {
				System.out.println("Unable to send GCM message.");
				System.out.println("Please ensure that API_KEY has been replaced by the server " +
						"API key, and that the device's registration token is correct (if specified).");
				e.printStackTrace();
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
}