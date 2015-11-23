package com.generador.capitalcycles.sofactory.interfaz;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PanelRutas extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel lblProyecto;
	private JTextField tfProyecto;
	private JLabel lblServidor;
	private JTextField tfServidor;
	private JLabel lblUsuarioBD;
	private JTextField tfUsuarioBD;
	private JLabel lblPassBD;
	private JPasswordField pfPassBD;
	private JLabel lblMaven;
	private JTextField tfMaven;
	
	public PanelRutas(){
		lblProyecto = new JLabel("Ruta del proyecto");
		tfProyecto = new JTextField(100);
		lblServidor = new JLabel("Ruta del servidor");
		tfServidor = new JTextField(100);
		lblUsuarioBD = new JLabel("Usuario de base de datos");
		tfUsuarioBD = new JTextField(10);
		lblPassBD= new JLabel("Password de base de datos");
		pfPassBD = new JPasswordField(10);
		lblMaven = new JLabel("Ruta de maven");
		tfMaven = new JTextField(100);
		
		GridLayout gl = new GridLayout(5,2);
		setLayout(gl);
		
		add(lblProyecto);
		add(tfProyecto);
		add(lblServidor);
		add(tfServidor);
		add(lblUsuarioBD);
		add(tfUsuarioBD);
		add(lblPassBD);
		add(pfPassBD);
		add(lblMaven);
		add(tfMaven);
		
		setVisible(true);
	}
	
	public String[] obtenerValores(){
		return new String[]{
			this.tfProyecto.getText(),
			this.tfServidor.getText(),
			this.tfUsuarioBD.getText(),
			new String(this.pfPassBD.getPassword()),
			this.tfMaven.getText()
		};
	}

}
