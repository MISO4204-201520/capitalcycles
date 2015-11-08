package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sofactory.entidades.BicicletaAlquiler;
import com.sofactory.entidades.EstacionEntrega;
import com.sofactory.entidades.Estado;
import com.sofactory.entidades.SitioAlquiler;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.SitioAlquilerBeanLocal;

@Stateless
@Local({SitioAlquilerBeanLocal.class})
public class SitioAlquilerBean extends GenericoBean<SitioAlquiler> implements SitioAlquilerBeanLocal{

	@PersistenceContext(unitName="RecorridosPU")
	private EntityManager em;

	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	@Override
	public List<BicicletaAlquiler> encontrarTodosBicicletaAlquiler() {
		TypedQuery<BicicletaAlquiler> typedQuery = em.createQuery("SELECT ba FROM BicicletaAlquiler ba ",BicicletaAlquiler.class);
		return typedQuery.getResultList();
	}


	@Override
	public List<EstacionEntrega> encontrarTodosEstacionEntrega() {
		TypedQuery<EstacionEntrega> typedQuery = em.createQuery("SELECT ee FROM EstacionEntrega ee ",EstacionEntrega.class);
		return typedQuery.getResultList();
	}

	@Override
	public List<BicicletaAlquiler> encontrarBicicletaAlquilerPorSitioAlquiler(Long codigoSitioAlquiler){
		TypedQuery<BicicletaAlquiler> typedQuery = em.createQuery("SELECT ba FROM BicicletaAlquiler ba LEFT JOIN FETCH ba.sitioAlquileres sa WHERE sa.codigo = :codigoSitioAlquiler",BicicletaAlquiler.class);
		typedQuery.setParameter("codigoSitioAlquiler", codigoSitioAlquiler);
		return typedQuery.getResultList();
	}

	@Override
	public Long bicicletasAlquiladas(Long codigoBicicleta, Long codigoSitio) {
		TypedQuery<Long> typedQuery = em.createQuery("SELECT COUNT(a) FROM Alquiler a LEFT JOIN  a.bicicletaAlquiler ba LEFT JOIN  a.sitioAlquiler sa WHERE ba.codigo = :codigoBicicleta AND sa.codigo = :codigoSitio AND a.estado = :estado ",Long.class);
		typedQuery.setParameter("codigoBicicleta", codigoBicicleta);
		typedQuery.setParameter("codigoSitio", codigoSitio);
		typedQuery.setParameter("estado", Estado.ALQUILADA);
		try{
			return typedQuery.getSingleResult();
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return 0l;
	}

	@Override
	public List<EstacionEntrega> encontrarEstacionEntregaPorSitioAlquiler(Long codigoSitioAlquiler) {
		TypedQuery<EstacionEntrega> typedQuery = em.createQuery("SELECT ee FROM EstacionEntrega ee LEFT JOIN FETCH ee.sitiosAlquileres sa WHERE sa.codigo = :codigoSitioAlquiler",EstacionEntrega.class);
		typedQuery.setParameter("codigoSitioAlquiler", codigoSitioAlquiler);
		return typedQuery.getResultList();
	}
}
