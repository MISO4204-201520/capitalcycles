package com.sofactory.dtos;

import java.io.Serializable;
import java.util.List;

public class BicicletaDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nombre;
	private Long codigoUsuario;
	private List<ParteDTO> partes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Long getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public List<ParteDTO> getPartes() {
		return partes;
	}

	public void setPartes(List<ParteDTO> partes) {
		this.partes = partes;
	}
}
