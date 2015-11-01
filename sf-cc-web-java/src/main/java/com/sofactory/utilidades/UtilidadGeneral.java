package com.sofactory.utilidades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UtilidadGeneral {
	
	public static InputStream clonarArchivo(InputStream archivo){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = archivo.read(buffer)) > -1 ) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return new ByteArrayInputStream(baos.toByteArray()); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static byte[] convertirArchivoByte(String archivo){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			InputStream fArchivo = new FileInputStream(new File(archivo)); 
			while ((len = fArchivo.read(buffer)) > -1 ) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			buffer = baos.toByteArray();
			baos.close();
			fArchivo.close();
			return buffer; 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static byte[] convertirArchivoByte(InputStream archivo){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = archivo.read(buffer)) > -1 ) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			buffer = baos.toByteArray();
			baos.close();
			archivo.close();
			return buffer; 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}