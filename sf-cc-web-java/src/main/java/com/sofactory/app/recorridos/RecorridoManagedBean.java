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
	
	private String inicio;
	
	private String fin;
	
	private MapModel mapModel;

	@PostConstruct
	private void iniciar(){
		
	}
	
	public void calcularRuta(){
		Long idUsuario = usuarioManagedBean.getUsuarioDTO().getCodigo();
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(R_MEJOR_RUTA+"/"+inicio+"/"+fin+"/"+idUsuario);
		RutaDTO ruta = messages.request("application/json")
				.get(RutaDTO.class);
		
		mapModel = new DefaultMapModel();
		PosicionDTO dto;
		
		Polyline polyLine = new Polyline();
		
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

	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}
}
