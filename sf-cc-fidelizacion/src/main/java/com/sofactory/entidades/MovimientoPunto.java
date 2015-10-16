package com.sofactory.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.sofactory.entidades.Producto;
import com.sofactory.entidades.Servicio;

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
	@JoinColumn(name="SERVICIO_ID")
	private Servicio servicio;
	
	@ManyToOne
	@JoinColumn(name="PRODUCTO_ID")
	private Producto producto;

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
}
