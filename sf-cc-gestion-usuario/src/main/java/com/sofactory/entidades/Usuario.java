package com.sofactory.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

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
}
