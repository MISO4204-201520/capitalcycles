package com.sofactory.negocio;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sofactory.entidades.Alquiler;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.AlquilerBeanLocal;

@Stateless
@Local({AlquilerBeanLocal.class})
public class AlquilerBean extends GenericoBean<Alquiler> implements AlquilerBeanLocal{

	@PersistenceContext(unitName="RecorridosPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
}
