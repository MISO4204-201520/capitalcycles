package com.sofactory.app.seguridad;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.sofactory.dtos.UsuarioDTO;

@ManagedBean
@SessionScoped
public class UsuarioManagedBean  implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private boolean logged;
	
	/** Atributo usuario. */
	private UsuarioDTO usuarioDTO;

	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}

	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}
}
