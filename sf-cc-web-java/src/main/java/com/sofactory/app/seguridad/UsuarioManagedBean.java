/*
 * Copyright © 2015, Secretaría General Alcaldía Mayor de Bogotá, Alta Consejería Distrital de TIC,
 * como se especifica en la etiqueta @author.
 *
 * Este material con derechos de autor se pondrá a disposición de cualquiera que desee
 * utilizar, modificar, copiar o redistribuir este programa sujeto a los términos y condiciones de
 * la GNU/GPL (General Public License) en su versión 3 o posteriores, según lo publicado por
 * Free Software Foundation. 
 * Usted podría obtener una copia de la licencia en:
 *
 *    http://www.gnu.org/licenses
 *
 * Ó ver el archivo LICENSE.txt, que se encuentra en el directorio raíz. 
 */
package com.sofactory.app.seguridad;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.sofactory.dtos.UsuarioDTO;

/**
 * UsuarioManagedBean, clase en sesion que permite guardar objetos del usuario
 * 
 * @author SECRETARIA GENERAL ALCALDIA MAYOR DE BOGOTA, ALTA CONSEJERIA DISTRITAL DE TIC - CIDT
 * 
 */
@ManagedBean
@SessionScoped
public class UsuarioManagedBean  implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** Atributo usuario. */
	private UsuarioDTO usuarioDTO;

	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}

	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}
}
