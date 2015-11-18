package com.generador.capitalcycles.sofactory.ejecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EjecutorComandos {
	public static void ejecutarMaven(){
		try {
			String salida = null;
			String [] cmd = {"cmd","/c","java","version"};

			Process p = Runtime.getRuntime().exec("shell\\maven.cmd"); 
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

	public static void main(String[] args) {
		ejecutarMaven();
	}
}
