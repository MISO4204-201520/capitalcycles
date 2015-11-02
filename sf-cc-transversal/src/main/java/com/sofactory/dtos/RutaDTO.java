package com.sofactory.dtos;

import java.util.List;

public class RutaDTO extends RespuestaDTO{

	protected Long id;
	
	protected String codigoUsuario;

	protected List<PosicionDTO> posiciones;

	private String direccionInicio;
	
	private String direccionFin;
	
	private String distanciaVisualizacion;
	
	private String tiempoTotalVisualizacion;
	
	public RutaDTO() {
		super();
	}

	public RutaDTO(Long id, String codigoUsuario) {
		super();
		this.id = id;
		this.codigoUsuario=codigoUsuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public List<PosicionDTO> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(List<PosicionDTO> posiciones) {
		this.posiciones = posiciones;
	}

	public String getDireccionInicio() {
		return direccionInicio;
	}

	public void setDireccionInicio(String direccionInicio) {
		this.direccionInicio = direccionInicio;
	}

	public String getDireccionFin() {
		return direccionFin;
	}

	public void setDireccionFin(String direccionFin) {
		this.direccionFin = direccionFin;
	}

	public String getDistanciaVisualizacion() {
		return distanciaVisualizacion;
	}

	public void setDistanciaVisualizacion(String distanciaVisualizacion) {
		this.distanciaVisualizacion = distanciaVisualizacion;
	}

	public String getTiempoTotalVisualizacion() {
		return tiempoTotalVisualizacion;
	}

	public void setTiempoTotalVisualizacion(String tiempoTotalVisualizacion) {
		this.tiempoTotalVisualizacion = tiempoTotalVisualizacion;
	}
}
