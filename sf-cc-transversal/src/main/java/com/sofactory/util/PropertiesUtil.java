package com.sofactory.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static Properties obtener(String archivo){
		Properties prop = new Properties();
		InputStream input = 
				PropertiesUtil.class.getClassLoader().getResourceAsStream(archivo);

		try {
			if(input==null){
				System.out.println("Sorry, unable to find " + archivo);
				return null;
			}
			//load a properties file from class path, inside static method
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return prop;
	}
}
