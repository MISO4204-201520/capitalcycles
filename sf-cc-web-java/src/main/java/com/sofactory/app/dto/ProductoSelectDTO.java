package com.sofactory.app.dto;

public class ProductoSelectDTO {
	
	private Long id;
	
	private String nombre;
	
	private Integer puntos;
	
	private Boolean seleccion;

	public ProductoSelectDTO(Long id, String nombre, Integer puntos, Boolean seleccion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.puntos = puntos;
		this.seleccion = seleccion;
	}

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

	public Integer getPuntos() {
		return puntos;
	}

	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}

	public Boolean getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(Boolean seleccion) {
		this.seleccion = seleccion;
	}
}
