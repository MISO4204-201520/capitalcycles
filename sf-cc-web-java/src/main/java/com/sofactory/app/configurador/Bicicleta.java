package com.sofactory.app.configurador;
 
import java.io.Serializable;
 
public class Bicicleta implements Serializable {
 
    private String marca;
	
    private String marco;
     
    private String tenedor;
    
    private String manubrio;
     
    private String llanta;
     
    private String freno;
    
    private String cadena;
    
    private String plato;
    
    private String cambio;
    
    private String traccion;

	public String getMarca() {
		return marca;
	}

	public String getMarco() {
		return marco;
	}

	public String getTenedor() {
		return tenedor;
	}
	
	public String getManubrio() {
		return manubrio;
	}

	public String getLlanta() {
		return llanta;
	}

	public String getFreno() {
		return freno;
	}

	public String getCadena() {
		return cadena;
	}

	public String getPlato() {
		return plato;
	}

	public String getCambio() {
		return cambio;
	}
	
	public String getTraccion() {
		return traccion;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public void setMarco(String marco) {
		this.marco = marco;
	}

	public void setTenedor(String tenedor) {
		this.tenedor = tenedor;
	}

	public void setManubrio(String manubrio) {
		this.manubrio = manubrio;
	}

	public void setLlanta(String llanta) {
		this.llanta = llanta;
	}

	public void setFreno(String freno) {
		this.freno = freno;
	}

	public void setCadena(String cadena) {
		this.cadena = cadena;
	}

	public void setPlato(String plato) {
		this.plato = plato;
	}

	public void setCambio(String cambio) {
		this.cambio = cambio;
	}
	
	public void setTraccion(String traccion) {
		this.traccion = traccion;
	}
}