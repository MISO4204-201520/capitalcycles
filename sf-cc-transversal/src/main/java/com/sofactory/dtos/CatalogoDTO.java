package com.sofactory.dtos;

import java.util.List;

public class CatalogoDTO extends RespuestaDTO{
	
	private List<ProductoDTO> productos;

	public List<ProductoDTO> getProductos() {
		return productos;
	}

	public void setProductos(List<ProductoDTO> productos) {
		this.productos = productos;
	}
}
