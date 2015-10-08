package com.sofactory.dtos;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDTO{
	private Long codigo;
	private String login;
	private String credencial;
	private String nombres;
	private String apellidos;
	private String celular;
	private String genero;
	private String correo;
	private List<RolDTO> roles;
	
	public UsuarioDTO(){}
	
	public UsuarioDTO(Long codigo, String login, String nombres, String apellidos, String celular, String genero,
			String correo) {
		this.codigo = codigo;
		this.login = login;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.celular = celular;
		this.genero = genero;
		this.correo = correo;
		roles = new ArrayList<RolDTO>();
	}
	
	public UsuarioDTO(Long codigo, String login, String credencial, String nombres, String apellidos, String celular, String genero,
			String correo) {
		this.codigo = codigo;
		this.credencial = credencial;
		this.login = login;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.celular = celular;
		this.genero = genero;
		this.correo = correo;
		roles = new ArrayList<RolDTO>();
	}
	
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
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public List<RolDTO> getRoles() {
		return roles;
	}
	public void setRoles(List<RolDTO> roles) {
		this.roles = roles;
	}
	public String getCredencial() {
		return credencial;
	}
	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}
}
