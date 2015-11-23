package com.generador.capitalcycles.sofactory.interfaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
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

	private JButton btnContinuar;
	
	public Ventana(){
		super("Generador");
		setLayout(new BorderLayout());
		
		panelRutas = new PanelRutas();
		btnContinuar = new JButton("Continuar");
		btnContinuar.addActionListener(this);
		
		add(panelRutas,BorderLayout.CENTER);
		add(btnContinuar,BorderLayout.SOUTH);

		setSize(400, 200);
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
			setVisible(false);
			String[] rutas = panelRutas.obtenerValores();
			Map<String,String> propiedades = new HashMap<>();
			propiedades.put("project.src", rutas[0].replace("\\", "\\\\"));
			propiedades.put("server.dir", rutas[1].replace("\\", "\\\\"));
			propiedades.put("user.db", rutas[2]);
			propiedades.put("pass.db", rutas[3]);
			propiedades.put("maven.dir", rutas[4].replace("\\", "\\\\"));
			
			try {
				ManejadorArchivos.generarArchivoPropieddades(propiedades, TipoPropiedades.USER_CONF);
				
				EjecutorComandos.ejecutarMaven(propiedades.get("maven.dir"),
						propiedades.get("project.src"));
				EjecutorComandos.ejecutarJboss(propiedades.get("server.dir"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
