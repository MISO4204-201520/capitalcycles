package com.sofactory.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "AL_SITIO_ALQUILER")
public class SitioAlquiler implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODIGO")
	private Long codigo;

	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="DIRECCION")
	private String direccion;
	
	@Column(name="LAT")
	private String lat;
	
	@Column(name="LNG")
	private String lng;

	@ManyToMany
	@JoinTable(
			name="AL_SITIO_BICICLETA",
			joinColumns={@JoinColumn(name="CODIGO_SITIO")},
			inverseJoinColumns={@JoinColumn(name="CODIGO_BICICLETA")},
			uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO_SITIO","CODIGO_BICICLETA"}))
	private List<BicicletaAlquiler> bicicletaAlquileres;
	
	@ManyToMany
	@JoinTable(
			name="AL_SITIO_ENTREGA",
			joinColumns={@JoinColumn(name="CODIGO_SITIO")},
			inverseJoinColumns={@JoinColumn(name="CODIGO_ENTREGA")},
			uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO_SITIO","CODIGO_ENTREGA"}))
	private List<EstacionEntrega> estacionEntregas;
	
	@OneToMany(mappedBy="sitioAlquiler")
	private List<Alquiler> alquileres;
	
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

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public List<BicicletaAlquiler> getBicicletaAlquileres() {
		return bicicletaAlquileres;
	}

	public void setBicicletaAlquileres(List<BicicletaAlquiler> bicicletaAlquileres) {
		this.bicicletaAlquileres = bicicletaAlquileres;
	}

	public List<EstacionEntrega> getEstacionEntregas() {
		return estacionEntregas;
	}

	public void setEstacionEntregas(List<EstacionEntrega> estacionEntregas) {
		this.estacionEntregas = estacionEntregas;
	}

	public List<Alquiler> getAlquileres() {
		return alquileres;
	}

	public void setAlquileres(List<Alquiler> alquileres) {
		this.alquileres = alquileres;
	}
}
