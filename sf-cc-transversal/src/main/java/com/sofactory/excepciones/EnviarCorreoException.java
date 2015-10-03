package com.sofactory.excepciones;

public class EnviarCorreoException extends Exception {

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Atributo mensaje. */
	private String mensaje;

	/**
	 * Constructor de la clase enviar correo exception.
	 *
	 * @param mensaje,
	 *            es el mensaje de la excepción
	 * @param excepcion,
	 *            es la excepción generada
	 */
	public EnviarCorreoException(String mensaje, Throwable excepcion) {
		super(excepcion);
		this.mensaje = mensaje;
	}

	/**
	 * Obtiene el atributo mensaje.
	 *
	 * @return el atributo mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Modifica el atributo mensaje.
	 *
	 * @param mensaje
	 *            es el nuevo valor para el atributo mensaje
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
