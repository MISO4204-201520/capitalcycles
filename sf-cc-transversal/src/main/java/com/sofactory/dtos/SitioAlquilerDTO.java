package com.sofactory.dtos;

import java.util.List;

public class SitioAlquilerDTO extends RespuestaDTO{
	private Long codigoSitioAlquiler;
	private Double latitud;
	private Double longitud;
	private String nombre;
	private String direccion;
	private List<BicicletaAlquilerDTO> bicicletaAlquilerDTOs;
	private List<EstacionEntregaDTO> estacionEntregaDTOs;
	
	public Double getLatitud() {
		return latitud;
	}
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	public Double getLongitud() {
		return longitud;
	}
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Long getCodigoSitioAlquiler() {
		return codigoSitioAlquiler;
	}
	public void setCodigoSitioAlquiler(Long codigoSitioAlquiler) {
		this.codigoSitioAlquiler = codigoSitioAlquiler;
	}
	public List<BicicletaAlquilerDTO> getBicicletaAlquilerDTOs() {
		return bicicletaAlquilerDTOs;
	}
	public void setBicicletaAlquilerDTOs(List<BicicletaAlquilerDTO> bicicletaAlquilerDTOs) {
		this.bicicletaAlquilerDTOs = bicicletaAlquilerDTOs;
	}
	public List<EstacionEntregaDTO> getEstacionEntregaDTOs() {
		return estacionEntregaDTOs;
	}
	public void setEstacionEntregaDTOs(List<EstacionEntregaDTO> estacionEntregaDTOs) {
		this.estacionEntregaDTOs = estacionEntregaDTOs;
	}
}
