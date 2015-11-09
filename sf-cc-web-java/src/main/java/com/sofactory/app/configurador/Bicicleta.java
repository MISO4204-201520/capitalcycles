package com.sofactory.app.configurador;
 
import java.io.Serializable;
 
public class Bicicleta implements Serializable {
 
    private String marca;
	
    private String marco;
     
    private String tenedor;
     
    private String rines;
     
    private String llantas;
     
    private String frenos;
    
    private String cadena;
    
    private String platos;
    
    private String cambios;

	public String getMarca() {
		return marca;
	}

	public String getMarco() {
		return marco;
	}

	public String getTenedor() {
		return tenedor;
	}

	public String getRines() {
		return rines;
	}

	public String getLlantas() {
		return llantas;
	}

	public String getFrenos() {
		return frenos;
	}

	public String getCadena() {
		return cadena;
	}

	public String getPlatos() {
		return platos;
	}

	public String getCambios() {
		return cambios;
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

	public void setRines(String rines) {
		this.rines = rines;
	}

	public void setLlantas(String llantas) {
		this.llantas = llantas;
	}

	public void setFrenos(String frenos) {
		this.frenos = frenos;
	}

	public void setCadena(String cadena) {
		this.cadena = cadena;
	}

	public void setPlatos(String platos) {
		this.platos = platos;
	}

	public void setCambios(String cambios) {
		this.cambios = cambios;
	}
}