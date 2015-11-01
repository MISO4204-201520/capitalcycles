package com.sofactory.negocio.interfaces;

import java.util.List;

import com.sofactory.entidades.Amigo;
import com.sofactory.negocio.general.MetodosGenerales;

public interface AmigoBeanLocal extends MetodosGenerales<Amigo>{

	List<Amigo> amigosDeUsuario(long codUser);
	
	boolean amigoDeUsuario(long codUser, long codAmigo);
	
}
