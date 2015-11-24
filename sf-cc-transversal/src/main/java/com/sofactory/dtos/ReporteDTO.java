package com.sofactory.dtos;

public class ReporteDTO extends RequestDTO{

	private Long id;
	
	private String reporte;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReporte() {
		return reporte;
	}

	public void setReporte(String reporte) {
		this.reporte = reporte;
	}
}
