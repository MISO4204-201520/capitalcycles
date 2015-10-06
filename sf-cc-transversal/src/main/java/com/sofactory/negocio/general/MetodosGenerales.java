package com.sofactory.negocio.general;

import java.util.List;

import javax.validation.ConstraintViolationException;

import com.sofactory.excepciones.ReferenciaException;
import com.sofactory.excepciones.RegistroYaExisteException;

public interface MetodosGenerales<T> {
	void insertar(T t) throws RegistroYaExisteException, ConstraintViolationException;
	List<T> encontrarTodos(Class<T> clase, String columnaOrden, String tipoOrden);
	T encontrarPorId(Class<T> clase, Object id);
	T insertarOActualizar(T t) throws ConstraintViolationException;
	void remover(T t, Object id) throws ReferenciaException;
	List<T> encontrarPorColumna(Class<T> clase, String columna, String valor);
}
