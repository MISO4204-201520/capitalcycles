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
import javax.validation.constraints.Null;

@Entity
@Table(name="RE_RUTA")
public class Ruta {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;
	
	@Column(name="DISTANCIA")
	@NotNull
	private Long distancia;
	
	@Column(name="TIEMPO_TOTAL")
	@NotNull
	private Long tiempoTotal;

	@Column(name="ES_PLANEADA")
	@NotNull
	private Boolean planeada;
	
	@ManyToOne
	@JoinColumn(name="RECORRIDO_ID")
	@Null
	private Recorrido recorrido;
	
//	@Column(name="USUARIO_ID")
//	@NotNull
//	private Usuario usuario;
	
	@OneToMany(mappedBy="ruta")
	private List<Posicion> posiciones;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDistancia() {
		return distancia;
	}

	public void setDistancia(Long distancia) {
		this.distancia = distancia;
	}

	public Long getTiempoTotal() {
		return tiempoTotal;
	}

	public void setTiempoTotal(Long tiempoTotal) {
		this.tiempoTotal = tiempoTotal;
	}
	
	public Boolean getPlaneada() {
		return planeada;
	}

	public void setPlaneada(Boolean planeada) {
		this.planeada = planeada;
	}

	public Recorrido getRecorrido() {
		return recorrido;
	}

	public void setRecorrido(Recorrido recorrido) {
		this.recorrido = recorrido;
	}

//	public Usuario getUsuario() {
//		return usuario;
//	}
//
//	public void setUsuario(Usuario usuario) {
//		this.usuario = usuario;
//	}

	public List<Posicion> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(List<Posicion> posiciones) {
		this.posiciones = posiciones;
	}
}
