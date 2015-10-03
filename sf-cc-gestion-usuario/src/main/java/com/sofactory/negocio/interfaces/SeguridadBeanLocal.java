package com.sofactory.negocio.interfaces;

import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.entidades.Usuario;
import com.sofactory.negocio.general.MetodosGenerales;

public interface SeguridadBeanLocal extends MetodosGenerales<Usuario>{
	RespuestaSeguridadDTO esValidoUsuario(String login, String credencial);
}
