package com.sofactory.negocio;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.sofactory.entidades.Usuario;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.UsuarioBeanLocal;

@Stateless
@Local({ UsuarioBeanLocal.class })
public class UsuarioBean extends GenericoBean<Usuario> implements UsuarioBeanLocal {

	private static final String GESTION_USUARIO_PU = "GestionUsuarioPU";
	
	public String saludo() {
		return "Saludo desde EJB";
	}
}
