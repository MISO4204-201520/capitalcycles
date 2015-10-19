package com.sofactory.dtos;

import java.util.ArrayList;
import java.util.List;

public class RespuestaMensajeDTO extends RespuestaDTO{

	private List<MensajeDTO> mensajes;
	
	public RespuestaMensajeDTO(){}
	
	public RespuestaMensajeDTO(int codigo, String mensaje) {
		super(codigo, mensaje);
		mensajes = new ArrayList<MensajeDTO>();
	}

	public List<MensajeDTO> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<MensajeDTO> mensajes) {
		this.mensajes = mensajes;
	}
	
}
