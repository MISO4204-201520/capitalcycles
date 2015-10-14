package com.sofactory.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PosicionOrden extends Posicion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8910870475397071611L;
	
	@Column(name="ORDEN")
	private Integer orden;

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

}
