package com.sofactory.negocio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sofactory.dtos.RespuestaSitioDTO;
import com.sofactory.dtos.SitioDTO;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.SitioBeanLocal;

@Stateless
@Local({SitioBeanLocal.class})
public class SitioBean extends GenericoBean<Object> implements SitioBeanLocal {

	@PersistenceContext(unitName="RecorridosPU")
	private EntityManager em;

	@EJB
	private RutaJPA rutaJPA;

	@EJB
	private PosicionJPA posicionJPA;

	public RespuestaSitioDTO encontrarSitios(String lat, String lng, String radio, String sitio) throws Exception {

		URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBS47G79uG86Dg6Avyg_qrYh7HIWnzPUsU&location="+lat+","+lng+"&radius="+radio+"&language=es&keyword="+sitio);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(url.openStream()));

		String inputLine;
		RespuestaSitioDTO respuestaSitioDTO = new RespuestaSitioDTO();
		respuestaSitioDTO.setCodigo(0);
		respuestaSitioDTO.setMensaje("OK");
		respuestaSitioDTO.setSitios(new ArrayList<SitioDTO>());
		SitioDTO sitioDTO= new SitioDTO();
		while ((inputLine = in.readLine()) != null){
			if (inputLine.contains("lat")){
				sitioDTO.setLatitud(new Double(inputLine.split(" : ")[1].split(",")[0]));
			}else if (inputLine.contains("lng")){
				sitioDTO.setLongitud(new Double(inputLine.split(" : ")[1].split(",")[0]));
			}else if (inputLine.contains("name")){
				sitioDTO.setNombre(inputLine.split(" : ")[1].split(",")[0]);
			}else if (inputLine.contains("vicinity")){
				sitioDTO.setDireccion(inputLine.split(" : ")[1]);
				respuestaSitioDTO.getSitios().add(sitioDTO);
				sitioDTO = new SitioDTO();
			}
		}
			
		in.close();


		return respuestaSitioDTO;
	}
}
