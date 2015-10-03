package com.sofactory.entidades;

import java.util.Date;

//@Entity
//@Table(name="RE_POSICION_TIEMPO")
public class PosicionTiempo extends Posicion {
	
	private static final long serialVersionUID = 1L;
	Date tiempo;

	public Date getTiempo() {
		return tiempo;
	}

	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}
}