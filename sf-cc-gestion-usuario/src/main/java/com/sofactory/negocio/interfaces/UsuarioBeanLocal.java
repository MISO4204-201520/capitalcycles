package com.sofactory.negocio.interfaces;

import com.sofactory.entidades.Usuario;
import com.sofactory.negocio.general.MetodosGenerales;

public interface UsuarioBeanLocal extends MetodosGenerales<Usuario>{
	Usuario encontrarPorLogin(Long codigo, String login);
}
