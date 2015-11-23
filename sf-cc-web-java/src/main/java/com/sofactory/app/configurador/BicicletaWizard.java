package com.sofactory.app.configurador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.EspecificacionDTO;
import com.sofactory.dtos.ListadoPartesDTO;
import com.sofactory.dtos.ParteDTO;
import com.sofactory.dtos.RespuestaBicicletaDTO;
import com.sofactory.utilidades.UtilidadGeneral;
 
@ManagedBean
@ViewScoped
public class BicicletaWizard implements Serializable {
 
	private static final long serialVersionUID = 1L;

	private static final String ARCHIVO_CONFIGURACION = "/com/sofactory/app/propiedades/configuradorBici.properties";
	
	private List<ParteDTO> parteDTOs;
	
	private List<ListadoPartesDTO> listadoPartesDTOs;
	
	private String nombreConfiguracion;
	
	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;
	
	private String getEncontrarListadoPartes = "http://localhost:8080/sf-cc-configurador-bici/rest/gestionarCBService/encontrarListadoPartes/";
	
	private String postCrearConfiguracion = "http://localhost:8080/sf-cc-configurador-bici/rest/gestionarCBService/crearConfiguracion";
	
	@PostConstruct
	private void iniciar(){
		InputStream in = BicicletaWizard.class.getResourceAsStream(ARCHIVO_CONFIGURACION);
		if (in!=null){
			try {
				List<String> partesAConfigurar = UtilidadGeneral.partes(in);
				if (partesAConfigurar!=null && !partesAConfigurar.isEmpty()){
					StringBuilder strPartes = new StringBuilder();
					int cont=0;
					for (String parte:partesAConfigurar){
						strPartes.append(parte);
						if (cont++<partesAConfigurar.size()-1){
							strPartes.append(",");
						}
					}
					Client client = ClientBuilder.newClient();
					WebTarget messages = client.target(getEncontrarListadoPartes+strPartes.toString());
					RespuestaBicicletaDTO respuesta = messages.request("application/json").get(RespuestaBicicletaDTO.class);
					if (respuesta!=null){
						if (respuesta.getCodigo()==0){
							listadoPartesDTOs = respuesta.getListadoPartesDTOs();
						}else{
							FacesContext.getCurrentInstance().addMessage(null, 
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR, 
											null, 
											respuesta.getMensaje()));
						}
					}
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

	public String guardar(){
		parteDTOs = new ArrayList<ParteDTO>();
		for (ListadoPartesDTO lp : listadoPartesDTOs){
			EspecificacionDTO especificacionSeleccionadaDTO = null;
			for (EspecificacionDTO e:lp.getEspecificaciones()){
				if (e.getCodigo().longValue()==lp.getEspecificacionSeleccionada().longValue()){
					especificacionSeleccionadaDTO = e;
					break;
				}
			}
			
			if (especificacionSeleccionadaDTO!=null){
				ParteDTO parteDTO = new ParteDTO();
				parteDTO.setCodigoUsuario(usuarioManagedBean.getUsuarioDTO().getCodigo());
				parteDTO.setColor(especificacionSeleccionadaDTO.getColor());
				parteDTO.setMarca(especificacionSeleccionadaDTO.getMarca());
				parteDTO.setPrecio(especificacionSeleccionadaDTO.getPrecio());
				parteDTO.setNombre(lp.getNombreFeauture());
				parteDTO.setClaseParte(lp.getNombreClaseParte());
				parteDTO.setNombreConfiguracion(nombreConfiguracion);
				parteDTOs.add(parteDTO);
			}
		}
		
		if (!parteDTOs.isEmpty()){
			//POST
			try{
				Client client = ClientBuilder.newClient();
				WebTarget messages = client.target(postCrearConfiguracion);
				RespuestaBicicletaDTO respuesta = messages.request("application/json").post(Entity.entity(parteDTOs, MediaType.APPLICATION_JSON),RespuestaBicicletaDTO.class);
				if (respuesta!=null){
					if (respuesta.getCodigo()==0){
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(
										FacesMessage.SEVERITY_INFO, 
										null, 
										"La configuración de la bicicleta se creó correctamente"));
						parteDTOs = null;
						nombreConfiguracion = null;
						for (ListadoPartesDTO lp : listadoPartesDTOs){
							lp.setEspecificacionSeleccionada(null);
						}
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
									"El servicio falló"));
				}
			}catch(Exception exc){
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								"Hubo un error en el sistema"));
				exc.printStackTrace();
			}		
		}
		return null;
	}
	
	public List<ListadoPartesDTO> getListadoPartesDTOs() {
		return listadoPartesDTOs;
	}

	public void setListadoPartesDTOs(List<ListadoPartesDTO> listadoPartesDTOs) {
		this.listadoPartesDTOs = listadoPartesDTOs;
	}

	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public String getNombreConfiguracion() {
		return nombreConfiguracion;
	}

	public void setNombreConfiguracion(String nombreConfiguracion) {
		this.nombreConfiguracion = nombreConfiguracion;
	}
}