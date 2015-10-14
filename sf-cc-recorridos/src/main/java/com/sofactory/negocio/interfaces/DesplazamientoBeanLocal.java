package com.sofactory.negocio.interfaces;

import javax.ejb.Local;
import javax.validation.ConstraintViolationException;

import com.sofactory.entidades.PosicionTiempo;
import com.sofactory.excepciones.RegistroYaExisteException;

@Local
public interface DesplazamientoBeanLocal {
	public void iniciarDesplazamiento(Long idRuta, String codigoUsuario) throws ConstraintViolationException, RegistroYaExisteException;
	public void registrarPosicion(PosicionTiempo posicion, Long idRuta);
}
