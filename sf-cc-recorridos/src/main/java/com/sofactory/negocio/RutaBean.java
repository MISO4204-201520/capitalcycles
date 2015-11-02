package com.sofactory.negocio;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.sofactory.entidades.Posicion;
import com.sofactory.entidades.Ruta;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.RutaBeanLocal;

@Stateless
@Local({RutaBeanLocal.class})
public class RutaBean extends GenericoBean<Posicion> implements RutaBeanLocal  {
	
	@PersistenceContext(unitName="RecorridosPU")
	private EntityManager em;
	
	@EJB
	private RutaJPA rutaJPA;
	
	@EJB
	private PosicionJPA posicionJPA;
	
	public Ruta encontrarMejor(Posicion origen, Posicion destino, String codigoUsuario) throws Exception{
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyBEelLTtoKYBwxxjM9nlkipXI4bO4BOK3g");

		DirectionsRoute[] ruta = DirectionsApi.getDirections(context, origen.toString(), destino.toString()).await();

		Ruta rutaP = new Ruta();
		rutaP.setDistancia(100l);
		rutaP.setPlaneada(true);
		rutaP.setTiempoTotal(100l);
		rutaP.setCodigoUsuario(codigoUsuario);
		rutaJPA.insertar(rutaP);
		
		rutaP.setPosiciones(new ArrayList<Posicion>());
		Posicion posicionRuta;
		for(int i=0; i < ruta[0].legs[0].steps.length; i++ ){
			posicionRuta = new Posicion();
			String[] splitPosicion = ruta[0].legs[0].steps[i].startLocation.toString().split(",");
			posicionRuta.setLatitud(new Double(splitPosicion[0]));
			posicionRuta.setLongitud(new Double(splitPosicion[1]));
			posicionRuta.setRuta(rutaP);
			posicionJPA.insertar(posicionRuta);
			rutaP.getPosiciones().add(posicionRuta);
		}
		
		return rutaP;
	}
}
