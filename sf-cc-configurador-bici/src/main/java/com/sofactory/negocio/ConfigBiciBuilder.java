package com.sofactory.negocio;

import java.util.ArrayList;

import com.sofactory.dtos.ParteDTO;
import com.sofactory.entidades.Bicicleta;
import com.sofactory.entidades.Parte;

public abstract class ConfigBiciBuilder {
	protected Bicicleta bicicleta;
	
	public Bicicleta getBicicleta(){
		return bicicleta;
	}
	
	public void crearBicicleta(){
		bicicleta = new Bicicleta();
		bicicleta.setPartes(new ArrayList<Parte>());
	}
	
	public abstract void construirParte(ParteDTO parte);
}