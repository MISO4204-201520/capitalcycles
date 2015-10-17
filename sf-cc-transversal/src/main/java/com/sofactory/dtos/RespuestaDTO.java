package com.sofactory.dtos;

public class RespuestaDTO extends RequestDTO{
	protected int codigo;
	protected String mensaje;
	
	public RespuestaDTO() {
		super();
	}

	public RespuestaDTO(int codigo, String mensaje) {
		this.codigo = codigo;
		this.mensaje = mensaje;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
