package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

	@Override
	public Usuario encontrarPorLogin(Long codigo, String login) {
		Usuario usuario = null;
		try {
			TypedQuery<Usuario> typedQuery = em.createQuery("SELECT u FROM Usuario u WHERE u.codigo <> :codigo AND u.login = :login ", Usuario.class);
			typedQuery.setParameter("codigo", codigo);
			typedQuery.setParameter("login", login);
			List<Usuario> lista = typedQuery.getResultList();
			if (lista!=null && !lista.isEmpty()){
				usuario = lista.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return usuario;
	}

	@Override
	public Boolean existeUsuarioRedSocial(String nombre, String redSocial) {
		boolean existe = false;
		try {
			TypedQuery<Usuario> typedQuery = em.createQuery("SELECT u FROM Usuario u WHERE u.nombres = :nombre AND u.redSocial = :redSocial ", Usuario.class);
			typedQuery.setParameter("nombre", nombre);
			typedQuery.setParameter("redSocial", redSocial);
			List<Usuario> lista = typedQuery.getResultList();
			if (lista!=null && !lista.isEmpty()){
				existe = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return existe;
	}

	@Override
	public Usuario encontrarPorRedSocial(String redSocial, String nombres) {
		Usuario usuario = null;
		try {
			TypedQuery<Usuario> typedQuery = em.createQuery("SELECT u FROM Usuario u WHERE u.nombres = :nombre AND u.redSocial = :redSocial ", Usuario.class);
			typedQuery.setParameter("nombre", nombres);
			typedQuery.setParameter("redSocial", redSocial);
			List<Usuario> lista = typedQuery.getResultList();
			if (lista!=null && !lista.isEmpty()){
				usuario = lista.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return usuario;
	}
}