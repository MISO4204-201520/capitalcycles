package com.sofactory.negocio;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import com.sofactory.dtos.UsuarioDTO;

/**
 * Session Bean implementation class UsuarioSingletonBean
 */
@Singleton
@LocalBean
public class UsuarioSingletonBean {
	
	private Map<Long, UsuarioDTO> usuariosDTO = new HashMap<Long,UsuarioDTO>();

	public Map<Long, UsuarioDTO> getUsuariosDTO() {
		return usuariosDTO;
	}

	public void setUsuariosDTO(Map<Long, UsuarioDTO> usuariosDTO) {
		this.usuariosDTO = usuariosDTO;
	}
}