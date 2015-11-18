package com.generador.capitalcycles.sofactory.interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.generador.capitalcycles.sofactory.archivo.ManejadorArchivos;
import com.generador.capitalcycles.sofactory.archivo.ManejadorArchivos.TipoPropiedades;
import com.generador.capitalcycles.sofactory.ejecutor.EjecutorComandos;
import com.generador.capitalcycles.sofactory.generador.GeneradorPropiedades;


public class Ventana extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PanelRutas panelRutas;
	
	public Ventana(){
		super("Generador");
		panelRutas = new PanelRutas(this);
		setContentPane(panelRutas);
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if("Continuar".equals(arg0.getActionCommand())){
			String[] rutas = panelRutas.obtenerValores();
			Map<String,String> propiedades = new HashMap<>();
			propiedades.put("project.src", rutas[0]);
			propiedades.put("server.dir", rutas[1]);
			propiedades.put("user.db", rutas[2]);
			propiedades.put("pass.db", rutas[3]);
			
			try {
				ManejadorArchivos.generarArchivoPropieddades(propiedades, TipoPropiedades.USER_CONF);
				
				EjecutorComandos.ejecutarMaven();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
