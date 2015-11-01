package com.sofactory.negocio.interfaces;

import java.util.List;

import com.sofactory.entidades.Mensaje;
import com.sofactory.negocio.general.MetodosGenerales;

public interface MensajeBeanLocal extends MetodosGenerales<Mensaje>{
	
	List<Mensaje> mensajesEnviadosPorUsuario(long codUser);
	
	List<Mensaje> mensajesRecibidosPorUsuario(long codUser);
}
