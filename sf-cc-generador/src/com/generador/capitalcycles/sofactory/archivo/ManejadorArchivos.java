package com.generador.capitalcycles.sofactory.archivo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ManejadorArchivos {
	
	public enum TipoPropiedades{
		FEATURES,
		FEATURES_EXCLUDES,
		USER_CONF;
	}
	
	public static List<String> obtenerConfigs(File config) throws IOException{
		List<String> configuracion = new ArrayList<String>();
		FileReader fr = new FileReader(config);
		BufferedReader br = new BufferedReader(fr);
		String linea = br.readLine();
		while(null!=linea){
			configuracion.add(linea);
			linea = br.readLine();
		}

		br.close();
		
		return configuracion;
	}
	
	public static void generarArchivoPropieddades
	(Map<String,?> properties, TipoPropiedades tipo) throws IOException{
		File archivo = new File("..\\sf-cc-transversal\\src\\main\\resources\\",
				tipo.name().toLowerCase()+".properties");
		archivo.getAbsolutePath();
		FileWriter fw = new FileWriter(archivo);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for (Entry<String, ?> entry : properties.entrySet()) {
			bw.append(entry.getKey()+"="+(entry.getValue().toString())+"\n");
		}
		
		bw.flush();
		bw.close();
	}

}
