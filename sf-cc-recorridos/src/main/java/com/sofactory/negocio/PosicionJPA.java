package com.sofactory.negocio;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sofactory.entidades.Posicion;
import com.sofactory.negocio.general.GenericoBean;

@Stateless
public class PosicionJPA extends GenericoBean<Posicion> {
	
	@PersistenceContext(unitName="RecorridosPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
}
