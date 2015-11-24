package com.sofactory.negocio;

import java.util.List;
import java.util.Properties;

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
	
	@Override
	public List<Reporte> obtenerTodos(){
		List<Reporte> reportes =
				reporteJpa.encontrarTodos(Reporte.class, "nombre", "asc");
		
		Properties prop = PropertiesUtil.obtener("/src/main/resources/features_excludes.properties");
		Reporte reporte;
		if (Boolean.parseBoolean(prop.getProperty("reportes.reportehistorialviajes.excludes"))){
			reporte = obtener(1l);
			reportes.remove(reporte);
		}
		if (Boolean.parseBoolean(prop.getProperty("reportes.reporterutas.excludes"))){
			reporte = obtener(2l);
			reportes.remove(reporte);
		}
		if (Boolean.parseBoolean(prop.getProperty("reportes.reportemetricas.excludes"))){
			reporte = obtener(3l);
			reportes.remove(reporte);
		}
		
		return reportes;
	}
}
