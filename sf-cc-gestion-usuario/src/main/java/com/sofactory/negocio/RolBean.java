package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sofactory.entidades.Rol;
import com.sofactory.enums.Estado;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.RolBeanLocal;

/**
 * Session Bean implementation class SeguridadBean
 */
@Stateless
@Local({RolBeanLocal.class})
public class RolBean extends GenericoBean<Rol> implements RolBeanLocal {
	
	@PersistenceContext(unitName="GestionUsuarioPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public List<Rol> encontrarRolesPorUsuario(Long codigoUsuario){
		List<Rol> roles = null;
		try {
			TypedQuery<Rol> typedQuery = em.createQuery("SELECT r FROM Rol r JOIN r.usuarios u WHERE u.codigo = :codigo AND (r.estado IS NULL OR r.estado = :estado) ", Rol.class);
			typedQuery.setParameter("codigo", codigoUsuario);
			typedQuery.setParameter("estado", Estado.ACTIVO);
			roles = typedQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return roles;
	}

	public Rol encontrarPorNombre(Integer id, String nombre) {
		Rol rol = null;
		try {
			TypedQuery<Rol> typedQuery = em.createQuery("SELECT r FROM Rol r WHERE r.id <> :id AND r.nombre = :nombre ", Rol.class);
			typedQuery.setParameter("id", id);
			typedQuery.setParameter("nombre", nombre);
			List<Rol> lista = typedQuery.getResultList();
			if (lista!=null && !lista.isEmpty()){
				rol = lista.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return rol;
	}
}
