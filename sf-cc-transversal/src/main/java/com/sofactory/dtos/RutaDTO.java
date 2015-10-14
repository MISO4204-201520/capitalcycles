package com.sofactory.dtos;

import java.util.List;

public class RutaDTO extends RespuestaDTO{

	protected Long id;
	
	protected String codigoUsuario;

	protected List<PosicionDTO> posiciones;
	
	public RutaDTO() {
		super();
	}

	public RutaDTO(Long id, String codigoUsuario) {
		super();
		this.id = id;
		this.codigoUsuario=codigoUsuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public List<PosicionDTO> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(List<PosicionDTO> posiciones) {
		this.posiciones = posiciones;
	}
}
