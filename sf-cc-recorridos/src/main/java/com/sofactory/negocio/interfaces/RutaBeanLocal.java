package com.sofactory.negocio.interfaces;

import com.sofactory.entidades.Posicion;
import com.sofactory.entidades.Ruta;
import com.sofactory.negocio.general.MetodosGenerales;

public interface RutaBeanLocal extends MetodosGenerales<Posicion>{
	Ruta encontrarMejor(Posicion origen, Posicion destino, String codigoUsuario) throws Exception;
}
