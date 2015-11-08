package com.sofactory.negocio;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;

import com.sofactory.entidades.PosicionTiempo;
import com.sofactory.entidades.Recorrido;
import com.sofactory.entidades.Ruta;
import com.sofactory.excepciones.RegistroYaExisteException;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.HistorialViajesBeanLocal;

import src.main.java.com.sofactory.negocio.interfaces.String;

@Stateless
public class HistorialViajesBean extends GenericoBean<Ruta> implements HistorialViajesBeanLocal{
	
	@PersistenceContext(unitName="HistorialViajesPU")
	private EntityManager em;
	
	public List<Ruta> getTodosViajes(String codigoUsuario)
	{
		Query query = em.createQuery("select r from Ruta r where r.codigoUsuario = :codigoUsuario");
		query.setParameter("codigoUsuario", codigoUsuario);
		
		List<Ruta> lstRespuesta = query.getResultList();
		
		return lstRespuesta;
		
	}
	
//	public List<Ruta> getViajesRangoFechas(String codigoUsuario, String fechaInicial, String fechaFinal)
//	{
//		Query query = em.createQuery("select r from Ruta r where r.codigoUsuario = :codigoUsuario and fecha >= :fechaInicial and fecha <= :fechaFinal");
//		query.setParameter("codigoUsuario", codigoUsuario);
//		query.setParameter("fechaInicial", fechaInicial);
//		query.setParameter("fechaFinal", fechaFinal);
//		
//		List<Ruta> lstRespuesta = query.getResultList();
//		
//		return lstRespuesta;
//	}
}
