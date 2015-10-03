package com.sofactory.negocio.interfaces;

import java.util.List;

import com.sofactory.entidades.Usuario;
import com.sofactory.excepciones.RegistroYaExisteException;

public interface UsuarioBeanLocal {
	void insertar(Usuario usuario) throws RegistroYaExisteException;

	List<Usuario> encontrarTodos();

	String saludo();
}
