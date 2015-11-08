package com.sofactory.dtos;

import java.util.Calendar;

public class AlquilerDTO{

	private Long codigo;

	private Long codigoBicicletaAlquiler;
	
	private Long codigoSitioAlquiler;
	
	private Long codigoUsuario;
	
	private Calendar fechaAlquiler;
	
	private Calendar fechaEntrega;
	
	private String estado;

	private Long limiteTiempoEntrega;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigoBicicletaAlquiler() {
		return codigoBicicletaAlquiler;
	}

	public void setCodigoBicicletaAlquiler(Long codigoBicicletaAlquiler) {
		this.codigoBicicletaAlquiler = codigoBicicletaAlquiler;
	}

	public Long getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public Calendar getFechaAlquiler() {
		return fechaAlquiler;
	}

	public void setFechaAlquiler(Calendar fechaAlquiler) {
		this.fechaAlquiler = fechaAlquiler;
	}
	
	public Calendar getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Calendar fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Long getLimiteTiempoEntrega() {
		return limiteTiempoEntrega;
	}

	public void setLimiteTiempoEntrega(Long limiteTiempoEntrega) {
		this.limiteTiempoEntrega = limiteTiempoEntrega;
	}

	public Long getCodigoSitioAlquiler() {
		return codigoSitioAlquiler;
	}

	public void setCodigoSitioAlquiler(Long codigoSitioAlquiler) {
		this.codigoSitioAlquiler = codigoSitioAlquiler;
	}
}
