package com.sofactory.negocio.interfaces;

import javax.ejb.Local;

import com.sofactory.entidades.PosicionTiempo;

@Local
public interface DesplazamientoEjbLocal {
	public void iniciarDesplazamiento(Long idRuta);
	public void registrarPosicion(PosicionTiempo posicion, Long idRuta);
}
