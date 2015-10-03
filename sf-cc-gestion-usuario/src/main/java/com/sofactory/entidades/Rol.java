package com.sofactory.entidades;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="GU_ROL")
public class Rol {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="NOMBRE", unique=true)
	private String nombre;

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
}
