package com.sofactory.negocio;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sofactory.entidades.MovimientoPunto;
import com.sofactory.negocio.general.GenericoBean;

@Stateless
public class MovimientoPuntoJPA extends GenericoBean<MovimientoPunto> {
	
	@PersistenceContext(unitName="FidelizacionPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public MovimientoPunto getMovimiento(Long idPunto){
		Query qry = this.em.createQuery
				("select m from MovimientoPunto where punto.id = :idPunto");
		qry.setParameter("idPunto", idPunto);
		
		return (MovimientoPunto) qry.getSingleResult();
	}
}

