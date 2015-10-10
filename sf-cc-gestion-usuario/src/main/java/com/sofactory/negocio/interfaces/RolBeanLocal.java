package com.sofactory.negocio.interfaces;

import java.util.List;

import com.sofactory.entidades.Rol;
import com.sofactory.negocio.general.MetodosGenerales;

public interface RolBeanLocal extends MetodosGenerales<Rol>{

	List<Rol> encontrarRolesPorUsuario(Long codigoUsuario);

	Rol encontrarPorNombre(Integer id, String nombre);
	
}
