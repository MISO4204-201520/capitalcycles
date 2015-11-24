package com.sofactory.negocio.interfaces;

import java.util.List;

import javax.ejb.Local;

import com.sofactory.entidades.Reporte;

@Local
public interface ReporteBeanLocal {
	Reporte obtener(Long id);
	
	List<?> obtenerDatos(Reporte reporte, String codigoUsuario);
	
	List<Reporte> obtenerTodos();
}
