package com.sofactory.dtos;

import java.util.ArrayList;
import java.util.List;

public class RespuestaAmigoDTO extends RespuestaDTO{

	private List<AmigoDTO> amigos;
	private List<UsuarioDTO> usuarioDTOs;
	
	public RespuestaAmigoDTO(){}
	
	public RespuestaAmigoDTO(int codigo, String mensaje) {
		super(codigo, mensaje);
		amigos = new ArrayList<AmigoDTO>();
	}

	public List<AmigoDTO> getAmigos() {
		return amigos;
	}

	public void setAmigos(List<AmigoDTO> amigos) {
		this.amigos = amigos;
	}

	public List<UsuarioDTO> getUsuarioDTOs() {
		return usuarioDTOs;
	}

	public void setUsuarioDTOs(List<UsuarioDTO> usuarioDTOs) {
		this.usuarioDTOs = usuarioDTOs;
	}
}
