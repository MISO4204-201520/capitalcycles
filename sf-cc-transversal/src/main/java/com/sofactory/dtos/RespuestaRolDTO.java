package com.sofactory.dtos;

import java.util.ArrayList;
import java.util.List;

public class RespuestaRolDTO extends RespuestaDTO{
	private List<RolDTO> roles;
	
	public RespuestaRolDTO(){
		super(0, "");
	}
	
	public RespuestaRolDTO(int codigo, String mensaje) {
		super(codigo, mensaje);
		roles = new ArrayList<RolDTO>();
	}

	public List<RolDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RolDTO> roles) {
		this.roles = roles;
	}
}