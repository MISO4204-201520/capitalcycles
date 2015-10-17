package com.sofactory.negocio;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sofactory.entidades.Producto;
import com.sofactory.negocio.general.GenericoBean;

@Stateless
public class ProductoJPA extends GenericoBean<Producto> {
	
	@PersistenceContext(unitName="FidelizacionPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public Producto getProducto(Long id){
		Query query = this.em.createQuery
				("select p from Producto p where p.id = :id");
		query.setParameter("id", id);
		
		return (Producto) query.getSingleResult();
	}
}
