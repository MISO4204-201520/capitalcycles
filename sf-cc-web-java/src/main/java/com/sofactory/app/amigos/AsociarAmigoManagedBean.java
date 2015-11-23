package com.sofactory.app.amigos;


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
import javax.faces.event.ActionEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.primefaces.component.commandbutton.CommandButton;

import com.sofactory.app.configurador.BicicletaWizard;
import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.AmigoDTO;
import com.sofactory.dtos.RespuestaAmigoDTO;
import com.sofactory.dtos.UsuarioDTO;

@ManagedBean
@ViewScoped
public class AsociarAmigoManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;

	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;

	private List<AmigoDTO> amigoDTOs;
	
	private List<UsuarioDTO> usuarioDTOs;

	private UsuarioDTO usuarioSeleccionado;
	
	private AmigoDTO amigoSeleccionado;

	private String getEncontrarUsuariosNoAmigos = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/encontrarUsuariosNoAmigos/";
	
	private String getAmigosDeUsuario = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/amigosDeUsuario/";
	
	private String postCrearAmigo = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/crearAmigo";

	private String deleteAmigo = "http://localhost:8080/sf-cc-mensajes/rest/mensajeService/removerAmigo/";
	
	private Long codigoUsuarioAmigo;
	
	private boolean visible;
	
	@PostConstruct
	private void iniciar(){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("features_excludes.properties");
		if (input!=null){
			Properties prop = new Properties();
			try {
				prop.load(input);
				String variabilidadAmigos = prop.getProperty("comunicacion.gestionamigos.excludes");
				if (variabilidadAmigos!=null){
					if (!new Boolean(variabilidadAmigos)){
						visible=true;
					}
				}else{
					visible=true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		amigoDTOs = new ArrayList<AmigoDTO>();
		String servicio=getAmigosDeUsuario+usuarioManagedBean.getUsuarioDTO().getCodigo();
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(servicio);
		RespuestaAmigoDTO respuesta = messages.request("application/json").get(RespuestaAmigoDTO.class);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("amigos");
		if (respuesta!=null && respuesta.getCodigo()==0){
			amigoDTOs  = respuesta.getAmigos();
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("amigos", amigoDTOs);
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							respuesta.getMensaje()));
		}
	}

	public List<UsuarioDTO> completarUsuarios(String query) {
		String servicio=getEncontrarUsuariosNoAmigos+usuarioManagedBean.getUsuarioDTO().getCodigo()+"/"+query;
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(servicio);
		RespuestaAmigoDTO respuesta = messages.request("application/json").get(RespuestaAmigoDTO.class);
		if (respuesta!=null && respuesta.getCodigo()==0){
			usuarioDTOs = respuesta.getUsuarioDTOs();
			return usuarioDTOs;
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							respuesta.getMensaje()));
		}
		
		return null;
	}

	public String asociar(){
		if (usuarioSeleccionado!=null){
			AmigoDTO amigoDTO = new AmigoDTO(); 
			amigoDTO.setCodAmigo(usuarioSeleccionado.getCodigo());
			amigoDTO.setCodUsuario(usuarioManagedBean.getUsuarioDTO().getCodigo());
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(postCrearAmigo);
			RespuestaAmigoDTO respuesta = messages.request("application/json").post(Entity.entity(amigoDTO, MediaType.APPLICATION_JSON),RespuestaAmigoDTO.class);
			if (respuesta!=null && respuesta.getCodigo()==0){
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(
								FacesMessage.SEVERITY_INFO, 
								null, 
								"Ya eres amigo del usuario: "+usuarioSeleccionado.getNombres()+" "+usuarioSeleccionado.getApellidos()));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								respuesta.getMensaje()));
			}
			usuarioSeleccionado = null;
			iniciar();
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							"Debe seleccionar un usuario a asociar como amigo"));
		}
			
		return null;
	}
	
	public void desasociar(ActionEvent evento){
		CommandButton boton = (CommandButton)evento.getSource();
		if (boton!=null){
			AmigoDTO amigoDTO = null;
			if (amigoDTOs!=null && !amigoDTOs.isEmpty()){
				for (AmigoDTO amigo:amigoDTOs){
					if (amigo.getId()==Long.parseLong(boton.getLabel())){
						amigoDTO = amigo;
						break;
					}
				}
				
				if (amigoDTO!=null){
					Client client = ClientBuilder.newClient();
					WebTarget messages = client.target(deleteAmigo+amigoDTO.getId()+"/"+amigoDTO.getCodUsuario()+"/"+amigoDTO.getCodAmigo());
					RespuestaAmigoDTO respuesta = messages.request().delete(RespuestaAmigoDTO.class);
					if (respuesta!=null && respuesta.getCodigo()==0){
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(
										FacesMessage.SEVERITY_INFO, 
										null, 
										"Se quita la asoción de amigos"));
					}else{
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										respuesta.getMensaje()));
					}
					usuarioSeleccionado = null;
					iniciar();
				}
			}
		}
	}
	
	public List<AmigoDTO> getAmigoDTOs() {
		return amigoDTOs;
	}

	public void setAmigoDTOs(List<AmigoDTO> amigoDTOs) {
		this.amigoDTOs = amigoDTOs;
	}

	public List<UsuarioDTO> getUsuarioDTOs() {
		return usuarioDTOs;
	}

	public void setUsuarioDTOs(List<UsuarioDTO> usuarioDTOs) {
		this.usuarioDTOs = usuarioDTOs;
	}

	public UsuarioDTO getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(UsuarioDTO usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public AmigoDTO getAmigoSeleccionado() {
		return amigoSeleccionado;
	}

	public void setAmigoSeleccionado(AmigoDTO amigoSeleccionado) {
		this.amigoSeleccionado = amigoSeleccionado;
	}

	public Long getCodigoUsuarioAmigo() {
		return codigoUsuarioAmigo;
	}

	public void setCodigoUsuarioAmigo(Long codigoUsuarioAmigo) {
		this.codigoUsuarioAmigo = codigoUsuarioAmigo;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}