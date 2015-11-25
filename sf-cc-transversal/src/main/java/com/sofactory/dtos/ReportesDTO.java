package com.sofactory.dtos;

import java.util.List;

public class ReportesDTO extends RespuestaDTO {
	
	private List<ReporteDTO> reporte;

	public ReportesDTO(int codigo, String mensaje) {
		super(codigo,mensaje);
	}

	public void setReporte(List<ReporteDTO> reporte) {
		this.reporte = reporte;
	}

	public List<ReporteDTO> getReporte() {
		return reporte;
	}
}
