package com.sofactory.dtos;

import java.util.ArrayList;
import java.util.List;

public class RespuestaUsuarioDTO extends RespuestaDTO{

	private List<UsuarioDTO> usuarios;
	
	public RespuestaUsuarioDTO (){}
	
	public RespuestaUsuarioDTO(int codigo, String mensaje) {
		super(codigo, mensaje);
		usuarios = new ArrayList<UsuarioDTO>();
	}

	public List<UsuarioDTO> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuarioDTO> usuarios) {
		this.usuarios = usuarios;
	}
}
