package com.sofactory.enums;

public enum ServicioPuntos {
	ENCONTRAR_MEJOR ("encontrarMejor", 100),
	INICIAR_DESPLAZAMIENTO("iniciarDesplazamiento",50),
	REGISTRAR_POSICION("registrarPosicion",20),
	REGISTRAR_POSICION_todos("registrarTodasPosiciones",50),
	OBTENER_TODOS_MENSAJES("obtenerTodosMensajes",50),
	OBTENER_MENSAJE_ID("encontrarMensajePorId",10),
	OBTENER_MESAJE_USUARIO("mensajesEnviadosPorUsuario",10),
	CREA_MENSAJE("RcrearNuevoMensaje",20);
	
	private String servicio;
	
	private Integer puntos;
	
	ServicioPuntos(String servicio, Integer puntos){
		this.servicio = servicio;
		this.puntos = puntos;
	}

	public String getServicio() {
		return servicio;
	}

	public Integer getPuntos() {
		return puntos;
	}
}
