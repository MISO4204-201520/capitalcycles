package com.sofactory.negocio.interfaces;

import javax.ejb.Local;
import javax.validation.ConstraintViolationException;

import com.sofactory.entidades.PosicionTiempo;
import com.sofactory.excepciones.RegistroYaExisteException;

@Local
public interface HistorialViajesBeanLocal {
	public void getTodosViajes(String codigoUsuario);
//	public void getViajesRangoFechas(String codigoUsuario, String fechaInicial, String fechaFinal);
}
