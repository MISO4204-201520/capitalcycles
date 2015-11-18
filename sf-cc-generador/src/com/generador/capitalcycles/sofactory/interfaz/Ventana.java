package com.generador.capitalcycles.sofactory.interfaz;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.generador.capitalcycles.sofactory.archivo.GeneradorPropiedades;

public class Ventana extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ventana(){
		super("Generador");
		setVisible(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Configuracion", "config");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File config = chooser.getSelectedFile();
			File xmlFeatures = new File("features/model.xml");
			
			try {
				new GeneradorPropiedades(xmlFeatures, config);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args){
		new Ventana();
	}
}
