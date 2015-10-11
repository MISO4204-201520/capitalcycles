package com.sofactory.entidades;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sofactory.enums.Estado;

@Entity
@Table(name="GU_ROL")
public class Rol {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="NOMBRE", unique=true)
	private String nombre;
	
	@Column(name = "ESTADO", nullable=true)
	@Enumerated(EnumType.STRING)
	private Estado estado = Estado.ACTIVO;

	@ManyToMany(mappedBy="roles")
	private List<Usuario> usuarios;
		
	@ManyToMany
	@JoinTable(
			name="GU_ROL_MENU",
			joinColumns={@JoinColumn(name="ID_ROL")},
			inverseJoinColumns={@JoinColumn(name="ID_MENU")},
			uniqueConstraints=@UniqueConstraint(columnNames={"ID_ROL","ID_MENU"}))
	protected List<Menu> menus;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
