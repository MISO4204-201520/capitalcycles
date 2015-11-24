package com.sofactory.servicios;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sofactory.entidades.Reporte;
import com.sofactory.negocio.interfaces.ReporteBeanLocal;
import com.sofactory.reportes.HibernateQueryResultDataSource;
import com.sofactory.util.ReporteUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Path("reporte")
public class ReporteServicio {

	@EJB
	private ReporteBeanLocal reporteBean;
	
	@Context
    private ServletContext context;

	@GET
	@Path("{reporteId}/{codigoUsuario}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/pdf")
	public Response obtener(
			@PathParam("reporteId")Long reporteId, 
			@PathParam("codigoUsuario")String codigoUsuario){
		File archivo = ReporteUtil.generarArchivo();
		try {
			generarReporte(archivo, reporteId, codigoUsuario);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
        ResponseBuilder response = Response.ok((Object) archivo);
		response.header("Content-Disposition",
				"attachment; filename="+archivo.getName());
		return response.build();
	}
	
	private void generarReporte(File archivo, Long reporteId, String codigoUsuario) throws JRException, FileNotFoundException{
		Reporte reporte = reporteBean.obtener(reporteId);
		List<?> lista = reporteBean.obtenerDatos(reporte, codigoUsuario);
		HibernateQueryResultDataSource ds = 
				new HibernateQueryResultDataSource(lista, reporte.getCampos().split(","));
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(
				context.getRealPath(ReporteUtil.RUTA_REPORTES)+"/"+reporte.getArchivo(), 
				new HashMap(),
				ds);

		JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new FileOutputStream(archivo)));
        exporter.exportReport();
	}
	
	@GET
	@Path("todos")
	@Produces(MediaType.APPLICATION_JSON)
	public ReportesDTO obtenerTodos(){
		ReportesDTO respuesta = new ReportesDTO(0, "OK");
		
		List<Reporte> reportes = reporteBean.obtenerTodos();
		
		respuesta.setReportes(new ArrayList<Reporte>());
		ReporteDTO dto;
		for(Reporte reporte : reportes){
			dto = new ReporteDTO();
			dto.setId(reporte.getId());
			dto.setNombre(reporte.getNombre());
			respuesta.getReportes().add(dto);
		}
		
		return respuesta;
	}
}
