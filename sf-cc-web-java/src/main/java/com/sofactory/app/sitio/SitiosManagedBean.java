package com.sofactory.app.sitio;

import java.io.Serializable;
import java.util.List;

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

import org.primefaces.model.map.MapModel;

import com.google.gson.Gson;
import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.RedSocialDTO;
import com.sofactory.dtos.RespuestaSitioDTO;
import com.sofactory.dtos.SitioDTO;

@ManagedBean
@ViewScoped
public class SitiosManagedBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;
	
	private String lat;
	
	private String lng;
	
	private MapModel mapModel;

	private String radio;
	
	private String sitio;
	
	private String otro;
	
	private String sitiosJson;
	
	private String getEncontrarSitios = "http://localhost:8080/sf-cc-recorridos/rest/sitio/encontrarSitios/";
	
	private String postRedSocialCompartir = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/redSocialCompartir/";
	
	private String facebookAccessTokenCompartir;
	
	private List<SitioDTO> sitioDTOs;
	
	private static final String TWITTER = "twitter";
	private static final String FACEBOOK = "facebook";
	
	@PostConstruct
	private void iniciar(){
		if (usuarioManagedBean!=null && usuarioManagedBean.getUsuarioDTO()!=null){
			if (usuarioManagedBean.getUsuarioDTO().getRedSocial()!=null && usuarioManagedBean.getUsuarioDTO().getRedSocial().equalsIgnoreCase(FACEBOOK)){
				//Este token es temporal para cada cuenta de facebook se debe crear un token, esto solo pasa con facebook.
				facebookAccessTokenCompartir = "CAACEdEose0cBAE3w8Vx2hDAFgnJc17inWZBZBwsUf7lLYLBQg9DOALurldPTRnXZCh3QxOCbpKZCHzNKI7YIbErafhBbjlGVBAZCAofHIpORlHMNxP8ikMfZCUwOpDl1Aan1ZB5JR2BuU3I9sGznhn2lQlrwkdCR12rMHmnQZCD9JV22D45KyWAZBNcCKv2qNzZAhSj7r4qZAVYmwZDZD";
			}
		}
	}

	public String buscarSitios(){
		if (otro!=null && !otro.isEmpty()){
			sitio = otro;
		}
		String servicio=getEncontrarSitios+lat+"/"+lng+"/"+radio+"/"+sitio.replaceAll(" ", "+")+"/"+usuarioManagedBean.getUsuarioDTO().getCodigo();
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(servicio);
		RespuestaSitioDTO respuesta = messages.request("application/json").get(RespuestaSitioDTO.class);
		if (respuesta!=null){
			if (respuesta.getCodigo()==0){
				Gson gson = new Gson();
				sitiosJson = gson.toJson(respuesta.getSitios());
				sitioDTOs = respuesta.getSitios();
			}
		}
		
		return null;
	}
	
	public void tweet(ActionEvent evento){
		StringBuilder stringBuilder = new StringBuilder("@"+usuarioManagedBean.getUsuarioDTO().getLogin()+" Sitios Cercanos de "+sitio+": \n");
		int cont=0;
		for (SitioDTO s:sitioDTOs){
			stringBuilder.append("Nombre: "+s.getNombre()+", ");
			stringBuilder.append("Direccion: "+s.getDireccion());
			if (cont++<sitioDTOs.size()-1){
				stringBuilder.append(", ");
			}
		}
		try{
			RedSocialDTO redSocialDTO = new RedSocialDTO();
			redSocialDTO.setRedSocial(TWITTER);
			redSocialDTO.setMensaje(stringBuilder.toString());
			redSocialDTO.setUsuarioToken(null);
			
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(postRedSocialCompartir);
			String respuesta = messages.request("application/json","text/plain").accept("application/json","text/plain").post(Entity.entity(redSocialDTO, MediaType.APPLICATION_JSON),String.class);
			if (respuesta!=null && respuesta.equalsIgnoreCase("OK")){
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_INFO, 
								null, 
								"Se realizó el tweet"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								"No se realizó el tweet"));
			}
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	public void facebook(ActionEvent evento){
		StringBuilder stringBuilder = new StringBuilder("Sitios Cercanos de "+sitio+": \n");
		int cont=0;
		for (SitioDTO s:sitioDTOs){
			stringBuilder.append("Nombre: "+s.getNombre()+", ");
			stringBuilder.append("Direccion: "+s.getDireccion());
			if (cont++<sitioDTOs.size()-1){
				stringBuilder.append(", ");
			}
		}
		try{
			RedSocialDTO redSocialDTO = new RedSocialDTO();
			redSocialDTO.setRedSocial(FACEBOOK);
			redSocialDTO.setMensaje(stringBuilder.toString());
			redSocialDTO.setUsuarioToken(facebookAccessTokenCompartir);
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(postRedSocialCompartir);
			String respuesta = messages.request("application/json","text/plain").accept("application/json","text/plain").post(Entity.entity(redSocialDTO, MediaType.APPLICATION_JSON),String.class);
			if (respuesta!=null && respuesta.equalsIgnoreCase("OK")){
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_INFO, 
								null, 
								"Se compartió en facebook"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								"No se compartió en facebook"));
			}
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public String getRadio() {
		return radio;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public String getSitio() {
		return sitio;
	}

	public void setSitio(String sitio) {
		this.sitio = sitio;
	}

	public String getOtro() {
		return otro;
	}

	public void setOtro(String otro) {
		this.otro = otro;
	}

	public String getSitiosJson() {
		return sitiosJson;
	}

	public void setSitiosJson(String sitiosJson) {
		this.sitiosJson = sitiosJson;
	}

	public List<SitioDTO> getSitioDTOs() {
		return sitioDTOs;
	}

	public void setSitioDTOs(List<SitioDTO> sitioDTOs) {
		this.sitioDTOs = sitioDTOs;
	}
}