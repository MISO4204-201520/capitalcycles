package com.sofactory.negocio;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sofactory.entidades.Recorrido;
import com.sofactory.entidades.Ruta;
import com.sofactory.negocio.general.GenericoBean;

@Stateless
public class RecorridoJPA extends GenericoBean<Recorrido> {

	@PersistenceContext(unitName="RecorridosPU")
	private EntityManager em;

	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}

	public Recorrido getRecorrido(Ruta ruta){
		Query query = em.createQuery("select r from Recorrido r where r.rutaPlaneada = :ruta");
		query.setParameter("ruta", ruta);

		try{
			return (Recorrido) query.getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
	}
}
