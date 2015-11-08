package com.sofactory.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "AL_ESTACION_ENTREGA")
public class EstacionEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODIGO")
	private Long codigo;

	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="DIRECCION")
	private String direccion;
	
	@ManyToMany(mappedBy="estacionEntregas")
	private List<SitioAlquiler> sitiosAlquileres;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public List<SitioAlquiler> getSitiosAlquileres() {
		return sitiosAlquileres;
	}

	public void setSitiosAlquileres(List<SitioAlquiler> sitiosAlquileres) {
		this.sitiosAlquileres = sitiosAlquileres;
	}	
}
