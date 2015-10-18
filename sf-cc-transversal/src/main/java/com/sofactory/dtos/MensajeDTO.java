package com.sofactory.dtos;

import java.util.Date;

public class MensajeDTO {

	private Long id;
	private Long usrdesde;
	private Long usrpara;
	private String texto;
	private Boolean status;
	private String fecha;
	private String loginUsuario;
	private String nombres;
	private String apellidos;
	private String loginUsuarioRecibe;
	private String nombresRecibe;
	private String apellidosRecibe;
	
	
	public MensajeDTO(){}
	
	public MensajeDTO(Long id, Long usrdesde, Long usrpara, String texto, Boolean status, String fecha) {
		super();
		this.id = id;
		this.usrdesde = usrdesde;
		this.usrpara = usrpara;
		this.texto = texto;
		this.status = status;
		this.fecha = fecha;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getUsrdesde() {
		return usrdesde;
	}
	
	public Long getUsrpara() {
		return usrpara;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public Boolean getStatus() {
		return status;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUsrdesde(Long usrdesde) {
		this.usrdesde = usrdesde;
	}
	
	public void setUsrpara(Long usrpara) {
		this.usrpara = usrpara;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
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

	public String getLoginUsuarioRecibe() {
		return loginUsuarioRecibe;
	}

	public void setLoginUsuarioRecibe(String loginUsuarioRecibe) {
		this.loginUsuarioRecibe = loginUsuarioRecibe;
	}

	public String getNombresRecibe() {
		return nombresRecibe;
	}

	public void setNombresRecibe(String nombresRecibe) {
		this.nombresRecibe = nombresRecibe;
	}

	public String getApellidosRecibe() {
		return apellidosRecibe;
	}

	public void setApellidosRecibe(String apellidosRecibe) {
		this.apellidosRecibe = apellidosRecibe;
	}
}
