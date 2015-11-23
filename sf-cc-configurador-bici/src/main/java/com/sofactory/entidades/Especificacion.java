package com.sofactory.entidades;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="CB_ESPECIFICACION")
public class Especificacion {
	@Id
	@Column(name="CODIGO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(name = "MARCA")
	private String marca;
	
	@Column(name = "PRECIO")
	private Double precio;
	
	@Column(name = "COLOR")
	private String color;
	
	@ManyToMany(mappedBy="especificaciones")
	private List<ListadoPartes> listadoPartes;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<ListadoPartes> getListadoPartes() {
		return listadoPartes;
	}

	public void setListadoPartes(List<ListadoPartes> listadoPartes) {
		this.listadoPartes = listadoPartes;
	}
}
