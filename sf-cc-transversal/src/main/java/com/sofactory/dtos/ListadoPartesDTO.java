package com.sofactory.dtos;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class ListadoPartesDTO {

	private Long codigo;
	private String nombreFeauture;
	private String nombrePadreFeature;
	private List<EspecificacionDTO> especificaciones;
	private String nombreClaseParte;
	private List<SelectItem> especificacionesSI;
	private Long especificacionSeleccionada;
	
	public ListadoPartesDTO(){
		especificaciones = new ArrayList<EspecificacionDTO>();
		especificacionesSI = new ArrayList<SelectItem>();
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

	public List<SelectItem> getEspecificacionesSI() {
		especificacionesSI = new ArrayList<SelectItem>();
		for (EspecificacionDTO e:especificaciones){
			SelectItem selectItem = new SelectItem(e.getCodigo(),e.getColor()+"-"+e.getMarca()+"-$"+e.getPrecio());
			especificacionesSI.add(selectItem);
		}
		return especificacionesSI;
	}

	public void setEspecificacionesSI(List<SelectItem> especificacionesSI) {
		this.especificacionesSI = especificacionesSI;
	}

	public Long getEspecificacionSeleccionada() {
		return especificacionSeleccionada;
	}

	public void setEspecificacionSeleccionada(Long especificacionSeleccionada) {
		this.especificacionSeleccionada = especificacionSeleccionada;
	}
}