package com.sofactory.negocio;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sofactory.entidades.Recorrido;
import com.sofactory.entidades.Ruta;
import com.sofactory.negocio.general.GenericoBean;

@Stateless
public class RutaJPA extends GenericoBean<Ruta> {

	@EJB
	private RecorridoJPA recorridoJPA;

	@PersistenceContext(unitName="RecorridosPU")
	private EntityManager em;

	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}

	public Ruta getDesplazamiento(Ruta ruta){ 
		Recorrido recorrido = recorridoJPA.getRecorrido(ruta);

		Query query = em.createQuery("select r from Ruta r where r.recorrido = :recorrido "
				+ "and r <> :ruta ");
		query.setParameter("recorrido", recorrido);
		query.setParameter("ruta", ruta);

		return (Ruta) query.getSingleResult();
	}

	public Ruta encontrarRutaRecorrido(Recorrido recorrido, String codigoUsuario){ 
		Query query = em.createQuery("select r from Ruta r where r.recorrido = :recorrido "
				+ "and codigoUsuario = :codigoUsuario ");
		query.setParameter("recorrido", recorrido);
		query.setParameter("codigoUsuario", codigoUsuario);

		try{
			return (Ruta) query.getSingleResult();
		}catch(NoResultException nre){
			return null;
		}
	}
}
