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
import com.sofactory.negocio.interfaces.DesplazamientoBeanLocal;

@Stateless
public class DesplazamientoBean extends GenericoBean<Ruta> implements DesplazamientoBeanLocal  {

	@EJB
	private RutaJPA rutaJpa;
	
	@EJB
	private RecorridoJPA recorridoJpa;
	
	@Override
	public void iniciarDesplazamiento(Long idRuta, String codigoUsuario) 
			throws ConstraintViolationException, RegistroYaExisteException {
		Ruta ruta = rutaJpa.encontrarPorId(Ruta.class, idRuta);
		Recorrido recorrido = recorridoJpa.getRecorrido(ruta);
		if (recorrido==null){
			recorrido = new Recorrido();
			recorrido.setDesplazamientos(new ArrayList<Ruta>());
			recorrido.setRutaPlaneada(ruta);
			recorrido.setNumeroUsuarios(0);
		}
		
		recorrido.setNumeroUsuarios(recorrido.getNumeroUsuarios()+1);
		recorridoJpa.insertarOActualizar(recorrido);
		
		Ruta desplazamiento = 
				rutaJpa.encontrarRutaRecorrido(recorrido, codigoUsuario);
		
		if (null!=desplazamiento){
			return;
		}
		desplazamiento = new Ruta();
		
		desplazamiento.setDistancia(0l);
		desplazamiento.setPlaneada(false);
		desplazamiento.setTiempoTotal(0l);
		desplazamiento.setRecorrido(recorrido);
		desplazamiento.setCodigoUsuario(codigoUsuario);
		
		rutaJpa.insertarOActualizar(desplazamiento);
	}

	@Override
	public void registrarPosicion(PosicionTiempo posicion, Long idRuta) {
		Ruta ruta = rutaJpa.encontrarPorId(Ruta.class, idRuta);
		Ruta recorrido = rutaJpa.getDesplazamiento(ruta);
		posicion.setRuta(recorrido);
		recorrido.getPosiciones().add(posicion);
		
		rutaJpa.insertarOActualizar(recorrido);
	}
}
