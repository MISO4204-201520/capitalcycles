package com.sofactory.negocio;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sofactory.entidades.Usuario;
import com.sofactory.excepciones.RegistroYaExisteException;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.UsuarioBeanLocal;

@Stateless
@Local({ UsuarioBeanLocal.class })
public class UsuarioBean extends GenericoBean<Usuario> implements UsuarioBeanLocal {

	private static final String GESTION_USUARIO_PU = "GestionUsuarioPU";

	@PersistenceContext(unitName = GESTION_USUARIO_PU)
	protected EntityManager em;

	public void insertar(Usuario u) throws RegistroYaExisteException {
		super.insertar(u);
	}

	public List<Usuario> encontrarTodos() {
		return super.encontrarTodos(Usuario.class);
	}

	public String saludo() {
		return "Saludo desde EJB";
	}
}
