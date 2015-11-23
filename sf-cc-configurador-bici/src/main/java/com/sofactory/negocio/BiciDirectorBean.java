package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import com.sofactory.dtos.ParteDTO;
import com.sofactory.entidades.Bicicleta;

@Singleton
public class BiciDirectorBean {
	
	private ConfigBiciBuilder configBiciBuilder;
	
	@PostConstruct
	private void iniciar(){
		configBiciBuilder = new BiciBuilder();
	}
	
	public void construirBicicleta(List<ParteDTO> partesDTO){
		configBiciBuilder.crearBicicleta();
		for (ParteDTO parte:partesDTO){
			configBiciBuilder.construirParte(parte);
		}
	}
	
	public Bicicleta getBicicleta(){
		return configBiciBuilder.getBicicleta();
	}
}