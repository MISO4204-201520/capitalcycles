package com.sofactory.negocio;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.sofactory.entidades.PosicionTiempo;
import com.sofactory.entidades.Recorrido;
import com.sofactory.entidades.Ruta;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.DesplazamientoEjbLocal;

@Stateless
public class DesplazamientoEjb extends GenericoBean<Ruta> implements DesplazamientoEjbLocal  {

	@Override
	public void iniciarDesplazamiento(Long idRuta) {
		Ruta ruta = em.find(Ruta.class, idRuta);
		Recorrido recorrido = getRecorrido(ruta);
		if (recorrido==null){
			recorrido = new Recorrido();
			recorrido.setDesplazamientos(new ArrayList<Ruta>());
			recorrido.setRuta(ruta);
		}
		
		Ruta desplazamiento = new Ruta();
		desplazamiento.setRecorrido(recorrido);

		em.persist(recorrido);
		em.persist(desplazamiento);
	}

	@Override
	public void registrarPosicion(PosicionTiempo posicion, Long idRuta) {
		Ruta ruta = em.find(Ruta.class, idRuta);
		Ruta recorrido = getDesplazamiento(ruta);
	}
	
	private Recorrido getRecorrido(Ruta ruta){
		Query query = em.createQuery("select r from Recorrido r where r.ruta = :ruta");
		query.setParameter("ruta", ruta);
		
		return (Recorrido) query.getSingleResult();
	}
	
	private Ruta getDesplazamiento(Ruta ruta){ 
		Recorrido recorrido = getRecorrido(ruta);
		
		Query query = em.createQuery("select r from Ruta r where r.recorrido = :recorrido "
				+ "and r <> :ruta ");
		query.setParameter("recorrido", recorrido);
		query.setParameter("ruta", ruta);
		
		return (Ruta) query.getSingleResult();
	}
}
