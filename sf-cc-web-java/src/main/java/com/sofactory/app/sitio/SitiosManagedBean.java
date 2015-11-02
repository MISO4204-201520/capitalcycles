package com.sofactory.app.sitio;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.primefaces.model.map.MapModel;

import com.google.gson.Gson;
import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.RespuestaSitioDTO;

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
	
	@PostConstruct
	private void iniciar(){
		
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
			}
		}
		
		return null;
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
}
