package com.sofactory.app.mensaje;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.AmigoDTO;
import com.sofactory.dtos.MensajeDTO;
import com.sofactory.dtos.RespuestaAmigoDTO;
import com.sofactory.dtos.RespuestaMensajeDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;

@ManagedBean
@ViewScoped
public class VerMensajesManagedBean implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;
	private String getMensajesRecibidos = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/mensajesRecibidosPorUsuario/";
	private String getMensajesEnviados = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/mensajesEnviadosPorUsuario/";
	private String getUsuarioPorCodigo = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarUsuarioPorCodigo/";
	private String getMensajePorId = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/encontrarMensajePorId/";
	private String putActualizarMensaje = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/actualizarMensaje";
	private String getEncontrarTodosUsuarios = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarTodosUsuarios";
	private String postCrearNuevoMensaje = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/crearNuevoMensaje";
	private String postEnviarCorreo = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/enviarCorreo";
	private String getAmigosDeUsuario = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/amigosDeUsuario/";
	private List<MensajeDTO> mensajesRecibidos;
	private List<MensajeDTO> mensajesEnviados;
	private boolean visibleVM = false;
	private String mensaje;
	private Long codigoUsuarioEnviar;
	private List<SelectItem> usuarios;
	private String mensajeAEnviar;
	private boolean visibleEM = false;
	private Integer opcion;
	private boolean visibleNotificacion;
	
	@PostConstruct
	private void iniciar(){
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("features_excludes.properties");
			if (input!=null){
				Properties prop = new Properties();
				prop.load(input);
				String variabilidadNotificacion = prop.getProperty("comunicacion.notificaciones.excludes");
				if (variabilidadNotificacion!=null){
					if (!new Boolean(variabilidadNotificacion)){
						visibleNotificacion=true;
					}
				}else{
					visibleNotificacion=true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mensajesRecibidos = new ArrayList<MensajeDTO>();
		mensajesEnviados = new ArrayList<MensajeDTO>();
		if (usuarioManagedBean!=null && usuarioManagedBean.getUsuarioDTO()!=null && usuarioManagedBean.getUsuarioDTO().getCodigo()!=null){
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(getMensajesRecibidos+usuarioManagedBean.getUsuarioDTO().getCodigo());
			RespuestaMensajeDTO respuesta = messages.request("application/json").get(RespuestaMensajeDTO.class);
			if (respuesta!=null){
				if (respuesta.getCodigo()==0){
					for (MensajeDTO m:respuesta.getMensajes()){
						String url=getUsuarioPorCodigo+m.getUsrdesde();
						client = ClientBuilder.newClient();
						messages = client.target(url);
						RespuestaUsuarioDTO r = messages.request("application/json").get(RespuestaUsuarioDTO.class);
						if (r!=null){
							if (r.getCodigo()==0 && r.getUsuarios()!=null && !r.getUsuarios().isEmpty()){
								m.setLoginUsuario(r.getUsuarios().get(0).getLogin());
								m.setNombres(r.getUsuarios().get(0).getNombres());
								m.setApellidos(r.getUsuarios().get(0).getApellidos());
							}
						}
						
						url=getUsuarioPorCodigo+m.getUsrpara();
						client = ClientBuilder.newClient();
						messages = client.target(url);
						RespuestaUsuarioDTO re = messages.request("application/json").get(RespuestaUsuarioDTO.class);
						if (re!=null){
							if (re.getCodigo()==0 && re.getUsuarios()!=null && !re.getUsuarios().isEmpty()){
								m.setLoginUsuarioRecibe(re.getUsuarios().get(0).getLogin());
								m.setNombresRecibe(re.getUsuarios().get(0).getNombres());
								m.setApellidosRecibe(re.getUsuarios().get(0).getApellidos());
							}
						}
					}
					this.mensajesRecibidos = respuesta.getMensajes();
				}
			}
			client = ClientBuilder.newClient();
			messages = client.target(getMensajesEnviados+usuarioManagedBean.getUsuarioDTO().getCodigo());
			respuesta = messages.request("application/json").get(RespuestaMensajeDTO.class);
			if (respuesta!=null){
				if (respuesta.getCodigo()==0){
					for (MensajeDTO m:respuesta.getMensajes()){
						String url=getUsuarioPorCodigo+m.getUsrdesde();
						client = ClientBuilder.newClient();
						messages = client.target(url);
						RespuestaUsuarioDTO r = messages.request("application/json").get(RespuestaUsuarioDTO.class);
						if (r!=null){
							if (r.getCodigo()==0 && r.getUsuarios()!=null && !r.getUsuarios().isEmpty()){
								m.setLoginUsuario(r.getUsuarios().get(0).getLogin());
								m.setNombres(r.getUsuarios().get(0).getNombres());
								m.setApellidos(r.getUsuarios().get(0).getApellidos());
							}
						}
						
						url=getUsuarioPorCodigo+m.getUsrpara();
						client = ClientBuilder.newClient();
						messages = client.target(url);
						RespuestaUsuarioDTO re = messages.request("application/json").get(RespuestaUsuarioDTO.class);
						if (re!=null){
							if (re.getCodigo()==0 && re.getUsuarios()!=null && !re.getUsuarios().isEmpty()){
								m.setLoginUsuarioRecibe(re.getUsuarios().get(0).getLogin());
								m.setNombresRecibe(re.getUsuarios().get(0).getNombres());
								m.setApellidosRecibe(re.getUsuarios().get(0).getApellidos());
							}
						}	
					}
					this.mensajesEnviados = respuesta.getMensajes();
				}
			}
		}
	}

	public void verMensajeEnviado(Long id){
		mensaje = null;
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(getMensajePorId+usuarioManagedBean.getUsuarioDTO().getCodigo()+"/"+id);
		RespuestaMensajeDTO respuesta = messages.request("application/json").get(RespuestaMensajeDTO.class);
		if (respuesta!=null && respuesta.getCodigo()==0 && respuesta.getMensajes()!=null &&
				!respuesta.getMensajes().isEmpty()){
			mensaje = respuesta.getMensajes().get(0).getTexto();
		}
		
		visibleVM=true;
	}
	
	public void verMensajeRecibido(Long id){
		mensaje = null;
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(getMensajePorId+usuarioManagedBean.getUsuarioDTO().getCodigo()+"/"+id);
		RespuestaMensajeDTO respuesta = messages.request("application/json").get(RespuestaMensajeDTO.class);
		if (respuesta!=null && respuesta.getCodigo()==0 && respuesta.getMensajes()!=null &&
				!respuesta.getMensajes().isEmpty()){
			mensaje = respuesta.getMensajes().get(0).getTexto();
			//PUT
			client = ClientBuilder.newClient();
			messages = client.target(putActualizarMensaje);
			MensajeDTO mensajeDTO = new MensajeDTO();
			mensajeDTO.setId(respuesta.getMensajes().get(0).getId());
			mensajeDTO.setUsrdesde(respuesta.getMensajes().get(0).getUsrdesde());
			mensajeDTO.setUsrpara(respuesta.getMensajes().get(0).getUsrpara());
			mensajeDTO.setTexto(respuesta.getMensajes().get(0).getTexto());
			RespuestaMensajeDTO respuestaMensaje = messages.request("application/json").put(Entity.entity(mensajeDTO, MediaType.APPLICATION_JSON),RespuestaMensajeDTO.class);
			if (respuestaMensaje!=null){
				if (respuestaMensaje.getCodigo()==0){
					iniciar();	
				}
				System.out.println("Respuesta Mensaje Leido "+respuesta.getCodigo());
			}
			
		}
		
		visibleVM=true;
	}
	
	public void cerrarVM(){
		mensaje = null;
		visibleVM=false;
	}
	
	public void abrirVentanaEnviar(Integer opcion){
		//Obtener Usuarios
		String servicio=getAmigosDeUsuario+usuarioManagedBean.getUsuarioDTO().getCodigo();
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(servicio);
		RespuestaAmigoDTO respuesta = messages.request("application/json").get(RespuestaAmigoDTO.class);
		if (respuesta!=null && respuesta.getCodigo()==0){
			List<AmigoDTO> amigoDTOs  = respuesta.getAmigos();
			usuarios = new ArrayList<SelectItem>();
			for (AmigoDTO a:amigoDTOs){
				SelectItem item = new SelectItem(a.getCodAmigo(), a.getNombres()+" "+a.getApellidos()+"-"+a.getCorreo());
				usuarios.add(item);
			}
			visibleEM = true;
			this.opcion = opcion;
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							respuesta.getMensaje()));
		}
	}
	
	public void cerrarEM(){
		visibleEM = false;
		this.mensajeAEnviar =null;
		this.opcion = null;
		this.codigoUsuarioEnviar = null;
	}
	
	public void enviarMensaje(){
		//POST
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(postCrearNuevoMensaje);
		MensajeDTO mensajeDTO = new MensajeDTO();
		mensajeDTO.setUsrdesde(usuarioManagedBean.getUsuarioDTO().getCodigo());
		mensajeDTO.setUsrpara(codigoUsuarioEnviar);
		mensajeDTO.setTexto(mensajeAEnviar);
		RespuestaMensajeDTO respuestaMensaje = messages.request("application/json").post(Entity.entity(mensajeDTO, MediaType.APPLICATION_JSON),RespuestaMensajeDTO.class);
		if (respuestaMensaje!=null){
			if (respuestaMensaje.getCodigo()==0){
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_INFO, 
								null, 
								"El mensaje se envió correctamente"));
				iniciar();
				cerrarEM();
			}else{
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								respuestaMensaje.getMensaje()));
			}			
		}
	}
	
	public void notificarMensaje(){
		//POST
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(postEnviarCorreo);
		MensajeDTO mensajeDTO = new MensajeDTO();
		mensajeDTO.setUsrdesde(usuarioManagedBean.getUsuarioDTO().getCodigo());
		mensajeDTO.setUsrpara(codigoUsuarioEnviar);
		mensajeDTO.setTexto(mensajeAEnviar);
		RespuestaMensajeDTO respuestaMensaje = messages.request("application/json").post(Entity.entity(mensajeDTO, MediaType.APPLICATION_JSON),RespuestaMensajeDTO.class);
		if (respuestaMensaje!=null){
			if (respuestaMensaje.getCodigo()==0){
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_INFO, 
								null, 
								"El mensaje se envió correctamente al correo del usuario destino"));
				iniciar();
				cerrarEM();
			}else{
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								respuestaMensaje.getMensaje()));
			}			
		}
		
	}
	
	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public String getGetMensajesRecibidos() {
		return getMensajesRecibidos;
	}

	public void setGetMensajesRecibidos(String getMensajesRecibidos) {
		this.getMensajesRecibidos = getMensajesRecibidos;
	}

	public String getGetMensajesEnviados() {
		return getMensajesEnviados;
	}

	public void setGetMensajesEnviados(String getMensajesEnviados) {
		this.getMensajesEnviados = getMensajesEnviados;
	}

	public List<MensajeDTO> getMensajesRecibidos() {
		return mensajesRecibidos;
	}

	public void setMensajesRecibidos(List<MensajeDTO> mensajesRecibidos) {
		this.mensajesRecibidos = mensajesRecibidos;
	}

	public List<MensajeDTO> getMensajesEnviados() {
		return mensajesEnviados;
	}

	public void setMensajesEnviados(List<MensajeDTO> mensajesEnviados) {
		this.mensajesEnviados = mensajesEnviados;
	}

	public boolean isVisibleVM() {
		return visibleVM;
	}

	public void setVisibleVM(boolean visibleVM) {
		this.visibleVM = visibleVM;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Long getCodigoUsuarioEnviar() {
		return codigoUsuarioEnviar;
	}

	public void setCodigoUsuarioEnviar(Long codigoUsuarioEnviar) {
		this.codigoUsuarioEnviar = codigoUsuarioEnviar;
	}

	public List<SelectItem> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<SelectItem> usuarios) {
		this.usuarios = usuarios;
	}

	public String getMensajeAEnviar() {
		return mensajeAEnviar;
	}

	public void setMensajeAEnviar(String mensajeAEnviar) {
		this.mensajeAEnviar = mensajeAEnviar;
	}

	public boolean isVisibleEM() {
		return visibleEM;
	}

	public void setVisibleEM(boolean visibleEM) {
		this.visibleEM = visibleEM;
	}

	public Integer getOpcion() {
		return opcion;
	}

	public void setOpcion(Integer opcion) {
		this.opcion = opcion;
	}

	public boolean isVisibleNotificacion() {
		return visibleNotificacion;
	}

	public void setVisibleNotificacion(boolean visibleNotificacion) {
		this.visibleNotificacion = visibleNotificacion;
	}
}
