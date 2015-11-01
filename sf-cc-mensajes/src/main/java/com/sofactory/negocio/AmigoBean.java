package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sofactory.entidades.Amigo;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.AmigoBeanLocal;

@Stateless
@Local({AmigoBeanLocal.class})
public class AmigoBean extends GenericoBean<Amigo> implements AmigoBeanLocal{

	@PersistenceContext(unitName="MensajesPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public List<Amigo> amigosDeUsuario(long codUser){
		
		Query query = em.createQuery("FROM com.sofactory.entidades.Amigo WHERE CODUSUARIO=:codUser ORDER BY CODAMIGO")
				.setParameter("codUser", codUser);
		List<Amigo> list = query.getResultList();
		
		return list;	
	}

	public boolean amigoDeUsuario(long codUser, long codAmigo){
		
		Query query = em.createQuery("FROM com.sofactory.entidades.Amigo WHERE CODUSUARIO=:codUser AND CODAMIGO=:codAmigo");
				query.setParameter("codUser", codUser);
				query.setParameter("codAmigo", codAmigo);
		List<Amigo> list = query.getResultList();
		
		return !list.isEmpty();	
	}
	
}