package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sofactory.entidades.Reporte;
import com.sofactory.negocio.general.GenericoBean;

@Stateless
public class ReporteJPA extends GenericoBean<Reporte> {
	
	@PersistenceContext(unitName="ReportesPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public List<?> realizarConsulta(String qry, String codigoUsuario){
		Query query = em.createNativeQuery(qry);
		query.setParameter("codigoUsuario", codigoUsuario);
		
		return query.getResultList();
	}
}
