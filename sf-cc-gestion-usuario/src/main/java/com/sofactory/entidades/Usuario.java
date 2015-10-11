package com.sofactory.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sofactory.enums.Estado;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "GU_USUARIO")
public abstract class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODIGO")
	protected Long codigo;

	@Column(name = "LOGIN", unique = true)
	protected String login;

	@Column(name = "PASSWORD")
	protected String password;

	@Column(name = "ESTADO", nullable=true)
	@Enumerated(EnumType.STRING)
	protected Estado estado = Estado.ACTIVO;
	
	@ManyToMany
	@JoinTable(
			name="GU_USUARIO_ROL",
			joinColumns={@JoinColumn(name="ID_USUARIO")},
			inverseJoinColumns={@JoinColumn(name="ID_ROL")},
			uniqueConstraints=@UniqueConstraint(columnNames={"ID_USUARIO","ID_ROL"}))
	protected List<Rol> roles;
	
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
