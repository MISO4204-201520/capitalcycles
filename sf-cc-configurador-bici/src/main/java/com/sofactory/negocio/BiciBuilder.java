package com.sofactory.negocio;

import com.sofactory.dtos.ParteDTO;
import com.sofactory.entidades.Parte;

public class BiciBuilder extends ConfigBiciBuilder{
	
	public void construirParte(ParteDTO parte){
		
		try {
			Class clase = Class.forName(parte.getClaseParte());
			Parte p = (Parte) clase.newInstance();
			p.setColor(parte.getColor());
			p.setMarca(parte.getMarca());
			p.setNombre(parte.getNombre());
			p.setPrecio(parte.getPrecio());
			p.setBicicleta(bicicleta);
			bicicleta.getPartes().add(p);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
}