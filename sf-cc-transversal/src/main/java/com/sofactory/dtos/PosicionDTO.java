package com.sofactory.dtos;

import java.util.Date;

public class PosicionDTO {

	protected Double latitud;
	
	protected Double longitud;
	
	protected Date tiempo;

	public PosicionDTO() {
		super();
	}

	public PosicionDTO(Double latitud, Double longitud, Date tiempo) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
		this.tiempo = tiempo;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	
	public Date getTiempo() {
		return tiempo;
	}

	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}

	@Override
	public String toString() {
		return latitud + ", " + longitud;
	}
}
