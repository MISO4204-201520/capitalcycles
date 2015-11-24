package src.main.java.com.sofactory.dtos;

import java.util.List;

public class ReportesDTO extends RespuestaDTO {
	
	private List<ReporteDTO> reporte;

	public List<ReporteDTO> getReporte() {
		return reporte;
	}

	public void setReporte(List<ReporteDTO> reporte) {
		this.reporte = reporte;
	}
}
