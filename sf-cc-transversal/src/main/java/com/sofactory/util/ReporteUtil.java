package com.sofactory.util;

import java.io.File;
import java.util.Date;

public class ReporteUtil {
	
	public final static String RUTA_REPORTES = "/reports";

	public final static String SO_TEMP = System.getProperty("java.io.tmpdir");
	
	public static File generarArchivo(){
		Date ahora = new Date();
		File reporte = new File(SO_TEMP, Long.toString(ahora.getTime())+".pdf");
		
		return reporte;
	}
}
