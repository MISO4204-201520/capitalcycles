package com.sofactory.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class PosicionTiempo extends Posicion {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="TIEMPO", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tiempo;

	public Date getTiempo() {
		return tiempo;
	}

	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}
}