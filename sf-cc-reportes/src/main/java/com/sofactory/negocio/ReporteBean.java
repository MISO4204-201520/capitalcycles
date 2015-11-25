package com.sofactory.negocio;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.sofactory.entidades.Reporte;
import com.sofactory.negocio.interfaces.ReporteBeanLocal;
import com.sofactory.util.PropertiesUtil;

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
		
		Reporte reporte;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("features_excludes.properties");
		if (input!=null){
			Properties prop = new Properties();
			try {
				prop.load(input);
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		return reportes;
	}
}
