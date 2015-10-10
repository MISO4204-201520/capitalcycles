package com.sofactory.dtos;

public class RespuestaSeguridadDTO extends RespuestaDTO{
	private Long codigoUsuario;
	private String login;
	private String credencial;
	private String nombres;
	private String apellidos;
	
	public RespuestaSeguridadDTO(){}
	
	public RespuestaSeguridadDTO(int codigo, String mensaje) {
		super(codigo, mensaje);
	}
	
	public Long getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getCredencial() {
		return credencial;
	}

	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
}
