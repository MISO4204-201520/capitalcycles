package src.main.java.com.sofactory.enums;

public enum Features {
	
	PROYECTO_BICICLETAS("proyectobicicletas");
	
	private String property;
	
	private Features(String property){
		this.property = property;
	}
	
	public String getProperty(){
		return this.property;
	}
}

