package com.sofactory.dtos;

public class RespuestaAlquilerDTO extends RespuestaDTO{
	private SitioAlquilerDTO sitioAlquilerDTO;

	public SitioAlquilerDTO getSitioAlquilerDTO() {
		return sitioAlquilerDTO;
	}

	public void setSitioAlquilerDTO(SitioAlquilerDTO sitioAlquilerDTO) {
		this.sitioAlquilerDTO = sitioAlquilerDTO;
	}
}
