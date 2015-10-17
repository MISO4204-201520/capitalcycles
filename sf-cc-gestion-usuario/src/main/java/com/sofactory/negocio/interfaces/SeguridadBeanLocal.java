package com.sofactory.negocio.interfaces;

import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Usuario;
import com.sofactory.negocio.general.MetodosGenerales;

public interface SeguridadBeanLocal extends MetodosGenerales<Usuario>{
	RespuestaSeguridadDTO esValidoUsuario(String login, String credencial);
	RespuestaSeguridadDTO obtenerUsuarioSesion(Long codigoUsuario);
	RespuestaSeguridadDTO cerrarSesion(Long codigoUsuario);
	String encriptar(String credencial);
	RespuestaUsuarioDTO cambiarCredencial(Usuario usuario, UsuarioDTO usuarioDTO);
}
