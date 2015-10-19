package com.sofactory.negocio.interfaz;

import java.util.List;

import javax.ejb.Local;
import javax.validation.ConstraintViolationException;

import com.sofactory.dtos.RedimirProductoDTO;
import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.entidades.MovimientoPunto;
import com.sofactory.entidades.Producto;
import com.sofactory.excepciones.RegistroYaExisteException;

@Local
public interface FidelizacionBeanLocal {
	void registrarServicio(RegistrarPuntosDTO dto) throws ConstraintViolationException, RegistroYaExisteException;
	void redimirProducto(RedimirProductoDTO dto) throws ConstraintViolationException, RegistroYaExisteException;
	List<Producto> obtenerCatalogoProdutos();
	Integer obtenerPuntosUsuario(String codigoUsuario);
	List<MovimientoPunto> obtenerHistorial(String codigoUsuario);
}
