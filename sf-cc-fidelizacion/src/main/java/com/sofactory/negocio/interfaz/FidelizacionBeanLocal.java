package com.sofactory.negocio.interfaz;

import javax.ejb.Local;

@Local
public interface FidelizacionBeanLocal {
	void registrarServicio();
	void redimirProducto();
	void obtenerCatalogoProdutos();
}
