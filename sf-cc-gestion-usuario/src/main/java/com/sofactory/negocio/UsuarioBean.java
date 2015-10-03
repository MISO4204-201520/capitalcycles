package com.sofactory.negocio;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sofactory.entidades.Usuario;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.UsuarioBeanLocal;

@Stateless
@Local({ UsuarioBeanLocal.class })
public class UsuarioBean extends GenericoBean<Usuario> implements UsuarioBeanLocal {

	private static final String GESTION_USUARIO_PU = "GestionUsuarioPU";

	@PersistenceContext(unitName = GESTION_USUARIO_PU)
	protected EntityManager em;
	
	public String saludo() {
		return "Saludo desde EJB";
	}
}
