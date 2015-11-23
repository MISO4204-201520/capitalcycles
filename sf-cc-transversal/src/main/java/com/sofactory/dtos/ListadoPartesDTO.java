package com.sofactory.dtos;

import java.util.ArrayList;
import java.util.List;

public class ListadoPartesDTO {

	private Long codigo;
	private String nombreFeauture;
	private String nombrePadreFeature;
	private List<EspecificacionDTO> especificaciones;
	private String nombreClaseParte;
	
	public ListadoPartesDTO(){
		especificaciones = new ArrayList<EspecificacionDTO>();
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNombreFeauture() {
		return nombreFeauture;
	}

	public void setNombreFeauture(String nombreFeauture) {
		this.nombreFeauture = nombreFeauture;
	}

	public String getNombrePadreFeature() {
		return nombrePadreFeature;
	}

	public void setNombrePadreFeature(String nombrePadreFeature) {
		this.nombrePadreFeature = nombrePadreFeature;
	}

	public List<EspecificacionDTO> getEspecificaciones() {
		return especificaciones;
	}

	public void setEspecificaciones(List<EspecificacionDTO> especificaciones) {
		this.especificaciones = especificaciones;
	}

	public String getNombreClaseParte() {
		return nombreClaseParte;
	}

	public void setNombreClaseParte(String nombreClaseParte) {
		this.nombreClaseParte = nombreClaseParte;
	}
}