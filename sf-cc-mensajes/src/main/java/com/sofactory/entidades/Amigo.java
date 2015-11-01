package com.sofactory.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ME_AMIGO")
public class Amigo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "CODUSUARIO")
	private Long codUsuario;

	@Column(name = "CODAMIGO")
	private Long codAmigo;

	public Long getId() {
		return id;
	}

	public Long getCodUsuario() {
		return codUsuario;
	}

	public Long getCodAmigo() {
		return codAmigo;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCodUsuario(Long codUsuario) {
		this.codUsuario = codUsuario;
	}

	public void setCodAmigo(Long codAmigo) {
		this.codAmigo = codAmigo;
	}

}