package com.sofactory.app.seguridad;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jasypt.util.text.BasicTextEncryptor;

import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.UsuarioDTO;

@ManagedBean
@ViewScoped
public class AutenticarUsuarioManagedBean implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Constante COD_ADMIN. */
	private static final Long COD_ADMIN = new Long(1);

	/** Constante MAXIMO_INTENTOS. */
	private static final Integer MAXIMO_INTENTOS = 3;

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

	/**
	 * Método - acción que sirve para iniciar la sesión de un usuario.
	 *
	 * @return Regla de navegación: CORRECTO, si el usuario se válido, null en caso contrario
	 */
	public String iniciarSesion(){
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
					usuarioManagedBean.setUsuarioDTO(usuarioAutenticado);
					reglaNavegacion = "CORRECTO";
				}else{
					FacesContext.getCurrentInstance().addMessage(null, 
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR, 
									null, 
									"El usuario no existe en el sistema"));
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
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
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
}
