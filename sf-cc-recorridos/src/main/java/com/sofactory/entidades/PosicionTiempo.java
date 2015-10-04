package com.sofactory.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="RE_POSICION_TIEMPO")
public class PosicionTiempo extends Posicion {
	
	private static final long serialVersionUID = 1L;
	private Date tiempo;

	public Date getTiempo() {
		return tiempo;
	}

	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}
}