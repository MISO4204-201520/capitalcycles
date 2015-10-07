package com.sofactory.dtos;

import java.math.BigDecimal;
import java.util.Date;

public class RegistrarPosicionDTO {

	private BigDecimal latitud;
	
	private BigDecimal longitud;
	
	private Date tiempo;
	
	private Long idRuta;

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

	public Date getTiempo() {
		return tiempo;
	}

	public void setTiempo(Date tiempo) {
		this.tiempo = tiempo;
	}

	public Long getIdRuta() {
		return idRuta;
	}

	public void setIdRuta(Long idRuta) {
		this.idRuta = idRuta;
	}
}
