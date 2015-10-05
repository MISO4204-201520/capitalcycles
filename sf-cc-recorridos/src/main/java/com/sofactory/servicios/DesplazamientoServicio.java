package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.Path;

import com.sofactory.entidades.Posicion;
import com.sofactory.entidades.Ruta;
import com.sofactory.negocio.interfaces.DesplazamientoBeanLocal;

@Path("desplazamiento")
public class DesplazamientoServicio {
	
	@EJB
	private DesplazamientoBeanLocal desplazamientoEjb;
	
	public void iniciarDesplazamiento(Ruta ruta){
		desplazamientoEjb.iniciarDesplazamiento(ruta.getId());
	}
	
	public void registrarPosicion(Posicion posicion){
		
	}
}
