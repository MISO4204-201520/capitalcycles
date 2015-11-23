package com.sofactory.entidades;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="CB_LISTADO_PARTES")
public class ListadoPartes {
	@Id
	@Column(name="CODIGO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	@Column(name = "NOMBRE_FEATURE")
	private String nombreFeauture;

	@Column(name = "NOMBRE_PADRE_FEATURE")
	private String nombrePadreFeature;
	
	@Column(name = "NOMBRE_CLASE_PARTE")
	private String nombreClaseParte;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="CB_LISTADO_PARTES_ESPECIFICACION",
			joinColumns={@JoinColumn(name="CODIGO_LISTADO_PARTES")},
			inverseJoinColumns={@JoinColumn(name="CODIGO_ESPECIFICACION")},
			uniqueConstraints=@UniqueConstraint(columnNames={"CODIGO_LISTADO_PARTES","CODIGO_ESPECIFICACION"}))
	private List<Especificacion> especificaciones;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNombreFeauture() {
		return nombreFeauture;
	}

	public void setNombreFeauture(String nombreFeauture) {
		this.nombreFeauture = nombreFeauture;
	}

	public String getNombrePadreFeature() {
		return nombrePadreFeature;
	}

	public void setNombrePadreFeature(String nombrePadreFeature) {
		this.nombrePadreFeature = nombrePadreFeature;
	}

	public List<Especificacion> getEspecificaciones() {
		return especificaciones;
	}

	public void setEspecificaciones(List<Especificacion> especificaciones) {
		this.especificaciones = especificaciones;
	}

	public String getNombreClaseParte() {
		return nombreClaseParte;
	}

	public void setNombreClaseParte(String nombreClaseParte) {
		this.nombreClaseParte = nombreClaseParte;
	}
}
