package com.sofactory.negocio;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sofactory.entidades.Punto;
import com.sofactory.negocio.general.GenericoBean;

@Stateless
public class PuntoJPA extends GenericoBean<Punto> {
	
	@PersistenceContext(unitName="FidelizacionPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
}
