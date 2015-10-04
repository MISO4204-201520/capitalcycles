package com.sofactory.negocio;

import javax.annotation.PostConstruct;
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

	@PersistenceContext(unitName="GestionUsuarioPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public String saludo() {
		return "Saludo desde EJB";
	}
}
