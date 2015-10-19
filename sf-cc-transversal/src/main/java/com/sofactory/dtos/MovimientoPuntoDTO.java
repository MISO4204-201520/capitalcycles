package com.sofactory.dtos;

import java.util.Date;

public class MovimientoPuntoDTO{
	
	private Boolean esRegistro;
	
	private String movimiento;
	
	private Integer puntos;
	
	private Date fecha;

	public Boolean getEsRegistro() {
		return esRegistro;
	}

	public void setEsRegistro(Boolean esRegistro) {
		this.esRegistro = esRegistro;
	}

	public String getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}

	public Integer getPuntos() {
		return puntos;
	}

	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
