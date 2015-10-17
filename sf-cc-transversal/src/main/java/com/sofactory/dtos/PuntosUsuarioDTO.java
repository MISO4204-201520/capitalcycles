package com.sofactory.dtos;

public class PuntosUsuarioDTO extends RespuestaDTO{

	private Integer puntos;
	
	private String codigoUsuario;

	public Integer getPuntos() {
		return puntos;
	}

	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
}
