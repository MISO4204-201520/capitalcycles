package com.sofactory.dtos;

import java.util.List;

public class HistorialPuntosDTO extends RespuestaDTO {
	
	private List<MovimientoPuntoDTO> movimientos;

	public List<MovimientoPuntoDTO> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<MovimientoPuntoDTO> movimientos) {
		this.movimientos = movimientos;
	}
}
