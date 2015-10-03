package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.Path;

import com.sofactory.entidades.Posicion;
import com.sofactory.entidades.Ruta;
import com.sofactory.negocio.interfaces.DesplazamientoEjbLocal;

@Path("desplazamiento")
public class DesplazamientoServicio {
	
	@EJB
	private DesplazamientoEjbLocal desplazamientoEjb;
	
	public void iniciarDesplazamiento(Ruta ruta){
		desplazamientoEjb.iniciarDesplazamiento(ruta.getId());
	}
	
	public void registrarPosicion(Posicion posicion){
		
	}
}
