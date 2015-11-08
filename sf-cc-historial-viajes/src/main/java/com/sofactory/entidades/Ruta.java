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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="RE_RUTA", 
	uniqueConstraints = {@UniqueConstraint(columnNames={"RECORRIDO_ID","CODIGO_USUARIO"})})
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
	
	@Column(name="DISTANCIA_VISUALIZACION")
	private String distanciaVisualizacion;
	
	@Column(name="TIEMPO_TOTAL_VISUALIZACION")
	private String tiempoTotalVisualizacion;
	
	@Column(name="DIRECCION_INICIO")
	private String direccionInicio;
	
	@Column(name="DIRECCION_FIN")
	private String direccionFin;
	
	@ManyToOne
	@JoinColumn(name="RECORRIDO_ID")
	private Recorrido recorrido;
	
	@Column(name="CODIGO_USUARIO")
	@NotNull
	private String codigoUsuario;
	
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

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigousuario) {
		this.codigoUsuario = codigousuario;
	}

	public List<Posicion> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(List<Posicion> posiciones) {
		this.posiciones = posiciones;
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
}
