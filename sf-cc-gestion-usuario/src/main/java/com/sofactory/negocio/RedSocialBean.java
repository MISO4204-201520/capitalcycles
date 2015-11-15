package com.sofactory.negocio;

import com.sofactory.dtos.UsuarioDTO;

public interface RedSocialBean {
	String obtenerUrl();
	UsuarioDTO obtenerUsuarioRedSocial(String verificador);
	void cerrarSession();
}
