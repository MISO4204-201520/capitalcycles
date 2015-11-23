package com.sofactory.dtos;

import java.util.ArrayList;
import java.util.List;

public class RespuestaBicicletaDTO extends RespuestaDTO{

	private List<ListadoPartesDTO> listadoPartesDTOs;
	private List<BicicletaDTO> bicicletaDTOs;
	
	public RespuestaBicicletaDTO (){}
	
	public RespuestaBicicletaDTO(int codigo, String mensaje) {
		super(codigo, mensaje);
		listadoPartesDTOs = new ArrayList<ListadoPartesDTO>();
	}
	
	public List<ListadoPartesDTO> getListadoPartesDTOs() {
		return listadoPartesDTOs;
	}

	public void setListadoPartesDTOs(List<ListadoPartesDTO> listadoPartesDTOs) {
		this.listadoPartesDTOs = listadoPartesDTOs;
	}

	public List<BicicletaDTO> getBicicletaDTOs() {
		return bicicletaDTOs;
	}

	public void setBicicletaDTOs(List<BicicletaDTO> bicicletaDTOs) {
		this.bicicletaDTOs = bicicletaDTOs;
	}
}
