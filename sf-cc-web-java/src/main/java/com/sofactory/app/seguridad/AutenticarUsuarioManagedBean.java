package com.sofactory.app.seguridad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jasypt.util.text.BasicTextEncryptor;

import com.sofactory.app.gestionusuario.ImagenPerfilManagedBean;
import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.UsuarioDTO;

@ManagedBean
@RequestScoped
public class AutenticarUsuarioManagedBean implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private static final String LLAVE_PASSWORD = "llavePassword?.";

	/** Atributo usuario. */
	private String usuario;

	/** Atributo contrasena. */
	private String contrasena;

	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;

	/** Atributo visibleCambioClave. */
	private boolean visibleCambioClave;

	/** Atributo nuevaClave. */
	private String nuevaClave;

	/** Atributo confirmarClave. */
	private String confirmarClave;

	private String post = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/esValidoUsuario";
	private String postCerrarSesion = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/cerrarSesion";
	private String postCambiarCredencial = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/cambiarCredencial";
	private String getRedSocialObtenerUrl = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/redSocialObtenerUrl/";
	private String postGuardarUsuarioRedSocial = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/guardarUsuarioRedSocial";
	private String getRedSocialCerrarSesion = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/redSocialCerrarSesion";
	
	private boolean verError;
	private String errorMensaje;
	private String urlTwitter;
	private List<String> redesSociales;
	
	private String urlFacebook;
	
	@ManagedProperty("#{imagenPerfilManagedBean}")
	private ImagenPerfilManagedBean imagenPerfilManagedBean;

	@PostConstruct
	private void iniciar(){
		urlTwitter = null;
		urlFacebook = null;
		redesSociales = new ArrayList<String>();
		redesSociales.add("twitter");
		redesSociales.add("facebook");
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		for (String redSocial:redesSociales){
			if ((usuarioManagedBean==null || usuarioManagedBean.getUsuarioDTO()==null) &&
					context.getRequestParameterMap()!=null && !context.getRequestParameterMap().isEmpty() &&
					((context.getRequestParameterMap().get("oauth_verifier")!=null && !context.getRequestParameterMap().get("oauth_verifier").isEmpty()) ||
							(context.getRequestParameterMap().get("code")!=null && !context.getRequestParameterMap().get("code").isEmpty()))){
				String verificador = context.getRequestParameterMap().get("oauth_verifier");
				if (verificador==null || verificador.isEmpty()){
					verificador = context.getRequestParameterMap().get("code");	
				}
				//POST
				try{
					Client client = ClientBuilder.newClient();
					WebTarget messages = client.target(postGuardarUsuarioRedSocial);
					UsuarioDTO usuarioDTO = new UsuarioDTO();
					usuarioDTO.setVerificador(verificador);
					usuarioDTO.setRedSocial(redSocial);
					RespuestaSeguridadDTO respuesta = messages.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
					if (respuesta!=null){
						if (respuesta.getCodigo()==0){
							UsuarioDTO usuarioAutenticado = new UsuarioDTO();
							usuarioAutenticado.setCodigo(new Long(respuesta.getCodigoUsuario()));
							usuarioAutenticado.setNombres(respuesta.getNombres());
							usuarioAutenticado.setApellidos(respuesta.getApellidos());
							usuarioAutenticado.setLogin(respuesta.getLogin());
							usuarioManagedBean.setUsuarioDTO(usuarioAutenticado);
							usuarioManagedBean.setLogged(true);
						}else{
							verError = true;
							errorMensaje = respuesta.getMensaje();
						}
					}else{
						verError = true;
						errorMensaje = "Hubo un error llamando el servicio";
					}
				}catch(Exception exc){
					verError = true;
					errorMensaje = "Hubo un error llamando el servicio";
					exc.printStackTrace();
				}		
		
			}else if(usuarioManagedBean==null || usuarioManagedBean.getUsuarioDTO()==null){
				//GET
				try{
					Client client = ClientBuilder.newClient();
					WebTarget messages = client.target(getRedSocialObtenerUrl+redSocial);
					String respuesta = messages.request("text/plain").get(String.class);
					if (respuesta!=null){
						if (redSocial.equalsIgnoreCase("twitter")){
							this.urlTwitter = respuesta;	
						}else if (redSocial.equalsIgnoreCase("facebook")){
							this.urlFacebook = respuesta;
						}
						
					}
				}catch(Exception exc){
					verError = true;
					errorMensaje = "Hubo un error llamando el servicio";
					exc.printStackTrace();
				}		
			}
		}
		
	}

	/**
	 * Método - acción que sirve para iniciar la sesión de un usuario.
	 *
	 * @return Regla de navegación: CORRECTO, si el usuario se válido, null en caso contrario
	 */
	public String iniciarSesion(){
		verError = false;
		errorMensaje = "";
		String reglaNavegacion = null;
		//POST
		try{
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(post);
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setLogin(this.usuario);
			BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
			textEncryptor.setPassword(LLAVE_PASSWORD);
			usuarioDTO.setCredencial(textEncryptor.encrypt(this.contrasena));
			RespuestaSeguridadDTO respuesta = messages.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
			if (respuesta!=null){
				if (respuesta.getCodigo()==0){
					UsuarioDTO usuarioAutenticado = new UsuarioDTO();
					usuarioAutenticado.setCodigo(new Long(respuesta.getCodigoUsuario()));
					usuarioAutenticado.setLogin(respuesta.getLogin());
					usuarioAutenticado.setNombres(respuesta.getNombres());
					usuarioAutenticado.setApellidos(respuesta.getApellidos());
					usuarioAutenticado.setCorreo(respuesta.getCorreo());
					usuarioAutenticado.setCredencial(respuesta.getCredencial());
					usuarioAutenticado.setFoto(respuesta.getFoto());
					imagenPerfilManagedBean.setImgActualizar(usuarioAutenticado.getFoto());
					usuarioManagedBean.setUsuarioDTO(usuarioAutenticado);
					usuarioManagedBean.setLogged(true);
					reglaNavegacion = "CORRECTO";
				}else{
					verError = true;
					errorMensaje = respuesta.getMensaje();
				}
			}else{
				verError = true;
				errorMensaje = "Hubo un error llamando el servicio";
			}
		}catch(Exception exc){
			verError = true;
			errorMensaje = "Hubo un error llamando el servicio";
			exc.printStackTrace();
		}		
		return reglaNavegacion;
	}

	/**
	 * Método - acción que sirve para cerrar la sesión de un usuario.
	 *
	 * @return Regla de navegación: CORRECTO, si se pudo cerrar correctamente la sesión, null en caso contrario
	 */
	public String cerrarSesion(){
		//POST
		try{
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(postCerrarSesion);
			UsuarioDTO usuarioDTO = new UsuarioDTO();
			usuarioDTO.setCodigo(usuarioManagedBean.getUsuarioDTO().getCodigo());
			messages.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
			//GET
			client = ClientBuilder.newClient();
			String redes = "";
			int cont = 0;
			for (String redSocial:redesSociales){
				redes+=redSocial;
				if (cont++ < redesSociales.size()-1){
					redes+=",";
				}
			}
			messages = client.target(getRedSocialCerrarSesion+"?redesSociales="+redes);
			messages.request("text/plain").get(String.class);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			usuarioManagedBean.setLogged(false);
		}catch(Exception exc){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							"Hubo un error llamando el servicio"));
			exc.printStackTrace();
		}		
		return "CORRECTO";
	}


	public String actualizarCredencial(){
		String reglaNavegacion = null;
		if (nuevaClave.equals(confirmarClave)){
			//POST
			try{
				Client client = ClientBuilder.newClient();
				WebTarget messages = client.target(postCambiarCredencial);
				BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
				textEncryptor.setPassword(LLAVE_PASSWORD);
				nuevaClave = textEncryptor.encrypt(nuevaClave);
				confirmarClave = textEncryptor.encrypt(confirmarClave);
				UsuarioDTO usuarioDTO = new UsuarioDTO();
				usuarioDTO.setCodigo(usuarioManagedBean.getUsuarioDTO().getCodigo());
				usuarioDTO.setCredencial(usuarioManagedBean.getUsuarioDTO().getCredencial());
				usuarioDTO.setCredencialNueva(nuevaClave);
				usuarioDTO.setConfirmacionCredencialNueva(confirmarClave);
				RespuestaUsuarioDTO respuesta = messages.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaUsuarioDTO.class);
				if (respuesta!=null){
					if (respuesta.getCodigo()==0){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_INFO, 
										null, 
										"El usuario cambió correctamente la credencial, para verificar cierre sesion e ingrese nuevamente"));
						usuarioDTO = new UsuarioDTO();
					}else{
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										respuesta.getMensaje()));
					}
				}else{
					FacesContext.getCurrentInstance().addMessage(null, 
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR, 
									null, 
									"Hubo un error llamando el servicio"));
				}
			}catch(Exception exc){
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								"Hubo un error llamando el servicio"));
				exc.printStackTrace();
			}		
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							"Credenciales no coinciden"));
		}


		return reglaNavegacion;
	}

	/**
	 * Obtiene el atributo usuario managed bean.
	 *
	 * @return el atributo usuario managed bean
	 */
	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	/**
	 * Modifica el atributo usuario managed bean.
	 *
	 * @param usuarioManagedBean es el nuevo valor para el atributo usuario managed bean
	 */
	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	/**
	 * Obtiene el atributo usuario.
	 *
	 * @return el atributo usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Modifica el atributo usuario.
	 *
	 * @param usuario es el nuevo valor para el atributo usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtiene el atributo contrasena.
	 *
	 * @return el atributo contrasena
	 */
	public String getContrasena() {
		return contrasena;
	}

	/**
	 * Modifica el atributo contrasena.
	 *
	 * @param contrasena es el nuevo valor para el atributo contrasena
	 */
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	/**
	 * Obtiene el atributo visibleCambioClave.
	 *
	 * @return el atributo visibleCambioClave
	 */
	public boolean isVisibleCambioClave() {
		return visibleCambioClave;
	}

	/**
	 * Modifica el atributo visibleCambioClave.
	 *
	 * @param visibleCambioClave es el nuevo valor para el atributo visibleCambioClave
	 */
	public void setVisibleCambioClave(boolean visibleCambioClave) {
		this.visibleCambioClave = visibleCambioClave;
	}

	/**
	 * Obtiene el atributo nuevaClave.
	 *
	 * @return el atributo nuevaClave
	 */
	public String getNuevaClave() {
		return nuevaClave;
	}

	/**
	 * Modifica el atributo nuevaClave.
	 *
	 * @param nuevaClave es el nuevo valor para el atributo nuevaClave
	 */
	public void setNuevaClave(String nuevaClave) {
		this.nuevaClave = nuevaClave;
	}

	/**
	 * Obtiene el atributo confirmarClave.
	 *
	 * @return el atributo confirmarClave
	 */
	public String getConfirmarClave() {
		return confirmarClave;
	}

	/**
	 * Modifica el atributo confirmarClave.
	 *
	 * @param confirmarClave es el nuevo valor para el atributo confirmarClave
	 */
	public void setConfirmarClave(String confirmarClave) {
		this.confirmarClave = confirmarClave;
	}

	public boolean isVerError() {
		return verError;
	}

	public void setVerError(boolean verError) {
		this.verError = verError;
	}

	public String getErrorMensaje() {
		return errorMensaje;
	}

	public void setErrorMensaje(String errorMensaje) {
		this.errorMensaje = errorMensaje;
	}

	public ImagenPerfilManagedBean getImagenPerfilManagedBean() {
		return imagenPerfilManagedBean;
	}

	public void setImagenPerfilManagedBean(ImagenPerfilManagedBean imagenPerfilManagedBean) {
		this.imagenPerfilManagedBean = imagenPerfilManagedBean;
	}

	public String getUrlTwitter() {
		return urlTwitter;
	}

	public void setUrlTwitter(String urlTwitter) {
		this.urlTwitter = urlTwitter;
	}

	public String getUrlFacebook() {
		return urlFacebook;
	}

	public void setUrlFacebook(String urlFacebook) {
		this.urlFacebook = urlFacebook;
	}
}
