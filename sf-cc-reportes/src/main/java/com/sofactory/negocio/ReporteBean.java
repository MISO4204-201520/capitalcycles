package com.sofactory.negocio;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.sofactory.entidades.Reporte;
import com.sofactory.negocio.interfaces.ReporteBeanLocal;

@Stateless
public class ReporteBean implements ReporteBeanLocal  {
	
	@EJB
	private ReporteJPA reporteJpa;
	
	@Override
	public Reporte obtener(Long id){
		return reporteJpa.encontrarPorId(Reporte.class, id);
	}

	@Override
	public List<?> obtenerDatos(Reporte reporte, String codigoUsuario) {
		return reporteJpa.realizarConsulta(reporte.getSentencia(), codigoUsuario);
	}
}
