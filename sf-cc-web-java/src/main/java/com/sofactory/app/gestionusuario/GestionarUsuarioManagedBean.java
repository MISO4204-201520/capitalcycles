package com.sofactory.app.gestionusuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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

import com.sofactory.app.gestionusuario.utilidades.UtilidadCorreo;
import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.RolDTO;
import com.sofactory.dtos.UsuarioDTO;

@ManagedBean
@ViewScoped
public class GestionarUsuarioManagedBean implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private static final String LLAVE_PASSWORD = "llavePassword?.";

	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;

	private UsuarioDTO usuarioDTO;

	private String postRegistrarUsuario = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/crear";
	private String putActualizarUsuario = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/actualizar";
	private String getUsuarioPorCodigo = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarUsuarioPorCodigo/";
	

	@PostConstruct
	private void iniciar(){
		usuarioDTO = new UsuarioDTO();
		if (usuarioManagedBean!=null && usuarioManagedBean.getUsuarioDTO()!=null && usuarioManagedBean.getUsuarioDTO().getCodigo()!=null){
			//Buscar Usuario
			getUsuarioPorCodigo+=usuarioManagedBean.getUsuarioDTO().getCodigo();
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(getUsuarioPorCodigo);
			RespuestaUsuarioDTO respuesta = messages.request("application/json").get(RespuestaUsuarioDTO.class);
			if (respuesta!=null){
				if (respuesta.getCodigo()==0){
					this.usuarioDTO = respuesta.getUsuarios().get(0);
				}
			}
		}
	}

	public String crear(){
		String reglaNavegacion = null;
		if (usuarioDTO.getCorreo()==null || (usuarioDTO.getCorreo()!=null && 
				UtilidadCorreo.validarFormatoCorreo(usuarioDTO.getCorreo().trim()))){
			if (usuarioDTO.getCredencialNueva().equals(usuarioDTO.getConfirmacionCredencialNueva())){
				//POST
				try{
					Client client = ClientBuilder.newClient();
					WebTarget messages = client.target(postRegistrarUsuario);
					BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
					textEncryptor.setPassword(LLAVE_PASSWORD);
					usuarioDTO.setCredencial(textEncryptor.encrypt(this.usuarioDTO.getCredencialNueva()));
					RolDTO rolDTO = new RolDTO();
					rolDTO.setId(2);
					List<RolDTO> roles = new ArrayList<RolDTO>();
					roles.add(rolDTO);
					usuarioDTO.setRoles(roles);
					usuarioDTO.setToken("T");
					RespuestaUsuarioDTO respuesta = messages.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaUsuarioDTO.class);
					if (respuesta!=null){
						if (respuesta.getCodigo()==0){
							FacesContext.getCurrentInstance().addMessage(null, 
									new FacesMessage(
											FacesMessage.SEVERITY_INFO, 
											null, 
											"El usuario se registró correctamente"));
							usuarioDTO = new UsuarioDTO();
						}else if (respuesta.getCodigo()==1){
							FacesContext.getCurrentInstance().addMessage(null, 
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR, 
											null, 
											"Faltan Campos Obligatorios"));
						}else if (respuesta.getCodigo()==2){
							FacesContext.getCurrentInstance().addMessage(null, 
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR, 
											null, 
											"El registro ya existe en la base de datos"));
						}else if (respuesta.getCodigo()==3){
							FacesContext.getCurrentInstance().addMessage(null, 
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR, 
											null, 
											"Campos con formatos invalidos"));
						}else if (respuesta.getCodigo()==4){
							FacesContext.getCurrentInstance().addMessage(null, 
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR, 
											null, 
											"El rol o roles ingresados no existen en el sistema"));
						}else{
							FacesContext.getCurrentInstance().addMessage(null, 
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR, 
											null, 
											"Hubo un error llamando el servicio"));
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
				FacesContext.getCurrentInstance().addMessage("formRegistrarUsuario:credencial", 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								"Credenciales no coinciden"));
			}
		}else{
			FacesContext.getCurrentInstance().addMessage("formRegistrarUsuario:correo", 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							"Correo invalido"));
		}

		return reglaNavegacion;
	}

	public String actualizar(){
		String reglaNavegacion = null;
		if (usuarioDTO.getCorreo()==null || (usuarioDTO.getCorreo()!=null && 
				UtilidadCorreo.validarFormatoCorreo(usuarioDTO.getCorreo().trim()))){

			try{
				//POST
				Client client = ClientBuilder.newClient();
				WebTarget messages = client.target(putActualizarUsuario);
				RespuestaUsuarioDTO respuesta = messages.request("application/json").put(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaUsuarioDTO.class);
				if (respuesta!=null){
					if (respuesta.getCodigo()==0){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_INFO, 
										null, 
										"El usuario se actualizó correctamente"));
					}else if (respuesta.getCodigo()==1){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										"Faltan Campos Obligatorios"));
					}else if (respuesta.getCodigo()==2){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										"Campos con formatos invalidos"));
					}else if (respuesta.getCodigo()==3){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										"El rol o roles ingresados no existen en el sistema"));
					}else if (respuesta.getCodigo()==4){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										"Hubo un error en el sistema"));
					}else if (respuesta.getCodigo()==5){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										"El login ya existe en el sistema"));
					}else if (respuesta.getCodigo()==6){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										"El usuario no existe en el sistema"));
					}else{
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_ERROR, 
										null, 
										"Hubo un error llamando el servicio"));
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
			FacesContext.getCurrentInstance().addMessage("formRegistrarUsuario:correo", 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							"Correo invalido"));
		}

		return reglaNavegacion;
	}
	
	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}

	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}

	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}
}
