package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sofactory.entidades.Mensaje;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.MensajeBeanLocal;

@Stateless
@Local({MensajeBeanLocal.class})
public class MensajeBean extends GenericoBean<Mensaje> implements MensajeBeanLocal{

	@PersistenceContext(unitName="MensajesPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public List<Mensaje> mensajesEnviadosPorUsuario(long codUser){
		
		Query query = em.createQuery("FROM com.sofactory.entidades.Mensaje WHERE USRDESDE=:codUser ORDER BY FECHA")
				.setParameter("codUser", codUser);
		List<Mensaje> list = query.getResultList();
		
		return list;
		
	}
	
	public List<Mensaje> mensajesRecibidosPorUsuario(long codUser){
	
		Query query = em.createQuery("FROM com.sofactory.entidades.Mensaje WHERE USRPARA=:codUser ORDER BY FECHA")
				.setParameter("codUser", codUser);
		List<Mensaje> list = query.getResultList();

		return list;
	}
	
}