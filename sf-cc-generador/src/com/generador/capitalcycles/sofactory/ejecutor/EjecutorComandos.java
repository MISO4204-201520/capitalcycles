package com.generador.capitalcycles.sofactory.ejecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EjecutorComandos {
	public static void ejecutarMaven(String rutaMaven, String rutaProyecto){
		try {
			Process p = Runtime.getRuntime().exec("shell/maven.cmd "+rutaMaven+" "+rutaProyecto+"\\sf-cc-raiz"); 
			BufferedReader in = new BufferedReader(  
					new InputStreamReader(p.getInputStream()));  
			String line = null;
			while ((line = in.readLine()) != null) {  
				System.out.println(line);
			} 
		} catch (IOException ioe) {
			System.out.println (ioe);
		}
	}
	
	public static void ejecutarJboss(String rutaJboss){
		try {
			Process p = Runtime.getRuntime().exec("shell/jboss-standalone.cmd "+rutaJboss); 
			BufferedReader in = new BufferedReader(  
					new InputStreamReader(p.getInputStream()));  
			String line = null;
			while ((line = in.readLine()) != null) {  
				System.out.println(line);
			} 
		} catch (IOException ioe) {
			System.out.println (ioe);
		}
		
		System.out.println("--------TERMINO");
	}

	public static void main(String[] args) {
		ejecutarJboss("C:\\Users\\Johan\\workspace\\fabricas\\proyecto\\wildfly-9.0.1.Final");
	}
}
