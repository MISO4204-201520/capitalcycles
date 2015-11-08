package com.sofactory.entidades;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="RE_RECORRIDO")
public class Recorrido {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;
	
	@Column(name="NUMERO_USUARIOS")
	@NotNull
	private Integer numeroUsuarios;
	
	@ManyToOne
	@JoinColumn(name="RUTA_PLANEADA_ID")
	@NotNull
	private Ruta rutaPlaneada;
	
	@OneToMany(mappedBy="recorrido")
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

	public Ruta getRutaPlaneada() {
		return rutaPlaneada;
	}

	public void setRutaPlaneada(Ruta rutaPlaneada) {
		this.rutaPlaneada = rutaPlaneada;
	}

	public List<Ruta> getDesplazamientos() {
		return desplazamientos;
	}

	public void setDesplazamientos(List<Ruta> desplazamientos) {
		this.desplazamientos = desplazamientos;
	}
}
