package com.sofactory.app.fidelizacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.HistorialPuntosDTO;
import com.sofactory.dtos.MovimientoPuntoDTO;

@ManagedBean
@ViewScoped
public class HistorialPuntosManagedBean implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private final static String R_OBTENER_HISTORIAL = "http://localhost:8080//sf-cc-fidelizacion/rest/fidelizacion/obtenerHistorial";

	private List<MovimientoPuntoDTO> registros;
	
	private List<MovimientoPuntoDTO> redenciones;
	
	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;
	
	@PostConstruct
	public void inicializar(){
		Long idUsuario = usuarioManagedBean.getUsuarioDTO().getCodigo();
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(R_OBTENER_HISTORIAL+"/"+idUsuario);
		HistorialPuntosDTO historial = messages.request("application/json")
				.get(HistorialPuntosDTO.class);
		
		registros = new ArrayList<MovimientoPuntoDTO>();
		redenciones = new ArrayList<MovimientoPuntoDTO>();
		
		for (MovimientoPuntoDTO dto : historial.getMovimientos()) {
			if (dto.getEsRegistro()){
				registros.add(dto);
			}else{
				redenciones.add(dto);
			}
		}
	}

	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public List<MovimientoPuntoDTO> getRegistros() {
		return registros;
	}

	public void setRegistros(List<MovimientoPuntoDTO> registros) {
		this.registros = registros;
	}

	public List<MovimientoPuntoDTO> getRedenciones() {
		return redenciones;
	}

	public void setRedenciones(List<MovimientoPuntoDTO> productos) {
		this.redenciones = productos;
	}
}
