package com.sofactory.dtos;

public class EncontrarRutaInDTO extends RequestDTO{

	protected String inicio;
	
	protected String fin;

	public EncontrarRutaInDTO() {
		super();
	}

	public EncontrarRutaInDTO(String inicio, String fin) {
		super();
		this.inicio = inicio;
		this.fin = fin;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}
}
