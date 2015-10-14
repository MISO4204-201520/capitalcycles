package com.sofactory.dtos;

public class PosicionDTO {

	protected Double latitud;
	
	protected Double longitud;

	public PosicionDTO() {
		super();
	}

	public PosicionDTO(Double latitud, Double longitud) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
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

	@Override
	public String toString() {
		return latitud + ", " + longitud;
	}
}
