package com.sofactory.dtos;

import java.util.List;

public class CatalogoDTO extends RespuestaDTO{
	
	private List<ProdutoDTO> productos;

	public List<ProdutoDTO> getProductos() {
		return productos;
	}

	public void setProductos(List<ProdutoDTO> productos) {
		this.productos = productos;
	}
}
