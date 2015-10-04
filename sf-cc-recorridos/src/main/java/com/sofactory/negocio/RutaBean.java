package com.sofactory.negocio;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sofactory.entidades.Posicion;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.RutaBeanLocal;

@Stateless
@Local({RutaBeanLocal.class})
public class RutaBean extends GenericoBean<Posicion> implements RutaBeanLocal  {
	
	@PersistenceContext(unitName="RecorridosPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
}
