package com.sofactory.app.gestionusuario.utilidades;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilidadCorreo {
	/**
	 * Metodo validarFormatoCorreo, funcion que se encarga de validar el formato del correo
	 * que es ingresado por parte del administrador de la aplicacion.
	 * 
	 * @param para, parametro que describe a quien va dirigido el correo.
	 * @return true si el formato es valido, false en caso contrario
	 */
	
	public static boolean validarFormatoCorreo(String para){
		Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9_\\-]+(\\.[A-Za-z0-9_\\-]+)*(\\.[A-Za-z_\\-]{2,})$");
		Matcher m = p.matcher(para);
		boolean correcto = false;
		if(m.find()){
			correcto = true;
		}
		return correcto;
	}
}
