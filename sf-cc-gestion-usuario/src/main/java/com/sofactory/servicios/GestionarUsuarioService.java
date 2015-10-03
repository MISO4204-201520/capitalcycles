package com.sofactory.servicios;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import com.sofactory.entidades.Usuario;
import com.sofactory.negocio.interfaces.UsuarioBeanLocal;

@Path("gestionarUsuarioService")
public class GestionarUsuarioService extends Application {

	@EJB
	private UsuarioBeanLocal usuarioBeanLocal;

	@Path("encontrarTodos")
	@GET
	@Produces("application/json")
	public List<?> encontrarTodos() {
		List<Usuario> usuarios = usuarioBeanLocal.encontrarTodos();
		return usuarios;
	}

	@Path("saludo")
	@GET
	public String saludo() {
		return usuarioBeanLocal.saludo();
	}

}
