package com.sofactory.entidades;

import java.util.List;

import javax.persistence.Id;

//@Entity
//@Table(name="RE_RECORRIDO")
public class Recorrido {

	@Id
	private Long id;
	
	private Integer numeroUsuarios;
	
	private Ruta ruta;
	
	private List<Ruta> desplazamientos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroUsuarios() {
		return numeroUsuarios;
	}

	public void setNumeroUsuarios(Integer numeroUsuarios) {
		this.numeroUsuarios = numeroUsuarios;
	}

	public Ruta getRuta() {
		return ruta;
	}

	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}

	public List<Ruta> getDesplazamientos() {
		return desplazamientos;
	}

	public void setDesplazamientos(List<Ruta> desplazamientos) {
		this.desplazamientos = desplazamientos;
	}
}
