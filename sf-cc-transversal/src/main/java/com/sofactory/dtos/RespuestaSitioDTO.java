package com.sofactory.dtos;

import java.util.List;

public class RespuestaSitioDTO extends RespuestaDTO{

	private List<SitioDTO> sitios;

	public List<SitioDTO> getSitios() {
		return sitios;
	}

	public void setSitios(List<SitioDTO> sitios) {
		this.sitios = sitios;
	}
}
