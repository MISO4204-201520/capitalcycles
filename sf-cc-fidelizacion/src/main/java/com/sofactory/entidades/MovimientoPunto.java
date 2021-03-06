package com.sofactory.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "FI_MOVIMIENTO_PUNTOS")
public class MovimientoPunto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "ES_REGISTO")
	@NotNull
	private Boolean registro;
	
	@ManyToOne
	@JoinColumn(name="PUNTO_ID")
	private Punto punto;
	
	@ManyToOne
	@JoinColumn(name="SERVICIO_ID")
	private Servicio servicio;
	
	@ManyToOne
	@JoinColumn(name="PRODUCTO_ID")
	private Producto producto;
	
	@Column(name="FECHA", columnDefinition="DATETIME")
	@Temporal(TemporalType.DATE)
	private Date fecha;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getRegistro() {
		return registro;
	}

	public void setRegistro(Boolean registro) {
		this.registro = registro;
	}
	
	public Punto getPunto() {
		return punto;
	}

	public void setPunto(Punto punto) {
		this.punto = punto;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
