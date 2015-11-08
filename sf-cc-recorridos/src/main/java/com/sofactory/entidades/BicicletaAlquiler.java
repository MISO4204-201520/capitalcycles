package com.sofactory.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "AL_BICICLETA_ALQUILER")
public class BicicletaAlquiler implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODIGO")
	private Long codigo;

	@Column(name="MODELO")
	private String modelo;
	
	@Column(name="MARCA")
	private String marca;

	@Column(name="TARIFA")
	private Double tarifa;
	
	@Lob
	@Column(name="FOTO")
	private byte[] foto;

	@Column(name="CANTIDAD")
	private Integer cantidad;
	
	@ManyToMany(mappedBy="bicicletaAlquileres")
	private List<SitioAlquiler> sitioAlquileres;
	
	@OneToMany(mappedBy="bicicletaAlquiler")
	private List<Alquiler> alquileres;
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getTarifa() {
		return tarifa;
	}

	public void setTarifa(Double tarifa) {
		this.tarifa = tarifa;
	}

	public List<SitioAlquiler> getSitioAlquileres() {
		return sitioAlquileres;
	}

	public void setSitioAlquileres(List<SitioAlquiler> sitioAlquileres) {
		this.sitioAlquileres = sitioAlquileres;
	}

	public List<Alquiler> getAlquileres() {
		return alquileres;
	}

	public void setAlquileres(List<Alquiler> alquileres) {
		this.alquileres = alquileres;
	}
}