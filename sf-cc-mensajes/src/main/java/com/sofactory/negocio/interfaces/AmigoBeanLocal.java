package com.sofactory.negocio.interfaces;

import java.util.List;

import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Amigo;
import com.sofactory.negocio.general.MetodosGenerales;

public interface AmigoBeanLocal extends MetodosGenerales<Amigo>{

	List<Amigo> amigosDeUsuario(long codUser);
	
	boolean amigoDeUsuario(long codUser, long codAmigo);

	List<UsuarioDTO> encontrarUsuariosNoAmigos(String completar, Long usuario, int maximoRegistros);
	
}
