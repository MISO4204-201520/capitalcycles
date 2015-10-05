package com.sofactory.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="RE_POSICION")
public class Posicion implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Long id;
	
	@Column(name="LATITUD")
	@NotNull
	private BigDecimal latitud;

	@Column(name="LONGITUD")
	@NotNull
	private BigDecimal longitud;

	@ManyToOne
	@JoinColumn(name="RUTA_ID")
	@Null
	private Ruta ruta;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

//	public Ruta getRuta() {
//		return ruta;
//	}
//
//	public void setRuta(Ruta ruta) {
//		this.ruta = ruta;
//	}
}
