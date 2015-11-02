package com.sofactory.app.recorridos;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.PosicionDTO;
import com.sofactory.dtos.RutaDTO;

@ManagedBean
@ViewScoped
public class RecorridoManagedBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static String R_MEJOR_RUTA = "http://localhost:8080///sf-cc-recorridos/rest/ruta/encontrarMejor";

	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;
	
	private String latInicio;
	
	private String lngInicio;
	
	private String latFin;
	
	private String lngFin;
	
	private MapModel mapModel;

	@PostConstruct
	private void iniciar(){
		
	}
	
	public void calcularRuta(){
		Long idUsuario = usuarioManagedBean.getUsuarioDTO().getCodigo();
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(R_MEJOR_RUTA+"/"+latInicio+"/"+lngInicio+"/"+latFin+"/"+lngFin+"/"+idUsuario);
		RutaDTO ruta = messages.request("application/json")
				.get(RutaDTO.class);
		
		mapModel = new DefaultMapModel();
		PosicionDTO dto;
		
		Polyline polyLine = new Polyline();
		polyLine.setStrokeColor("#DF7401");
		polyLine.setStrokeWeight(4);
		for (int i = 0; i < ruta.getPosiciones().size(); i++) {
			dto = ruta.getPosiciones().get(i);
			polyLine.getPaths().add(new LatLng(dto.getLatitud(),dto.getLongitud()));
			if (i==0){
				mapModel.addOverlay(new Marker(
						new LatLng(dto.getLatitud(),dto.getLongitud()),"Inicio"));
			}else if(i==ruta.getPosiciones().size()-1){
				mapModel.addOverlay(new Marker(
						new LatLng(dto.getLatitud(),dto.getLongitud()),"Fin"));
			}
		}
		
		mapModel.addOverlay(polyLine);
	}

	public void limpiarMapa(){
		mapModel = new DefaultMapModel();
	}
	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public String getLatInicio() {
		return latInicio;
	}

	public void setLatInicio(String latInicio) {
		this.latInicio = latInicio;
	}

	public String getLngInicio() {
		return lngInicio;
	}

	public void setLngInicio(String lngInicio) {
		this.lngInicio = lngInicio;
	}

	public String getLatFin() {
		return latFin;
	}

	public void setLatFin(String latFin) {
		this.latFin = latFin;
	}

	public String getLngFin() {
		return lngFin;
	}

	public void setLngFin(String lngFin) {
		this.lngFin = lngFin;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}
}
