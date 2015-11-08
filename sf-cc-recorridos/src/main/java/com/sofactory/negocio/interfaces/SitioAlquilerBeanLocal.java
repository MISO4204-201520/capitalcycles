package com.sofactory.negocio.interfaces;

import java.util.List;

import com.sofactory.entidades.BicicletaAlquiler;
import com.sofactory.entidades.EstacionEntrega;
import com.sofactory.entidades.SitioAlquiler;
import com.sofactory.negocio.general.MetodosGenerales;

public interface SitioAlquilerBeanLocal extends MetodosGenerales<SitioAlquiler>{

	List<BicicletaAlquiler> encontrarTodosBicicletaAlquiler();

	List<EstacionEntrega> encontrarTodosEstacionEntrega();

	List<BicicletaAlquiler> encontrarBicicletaAlquilerPorSitioAlquiler(Long codigoSitioAlquiler);

	Long bicicletasAlquiladas(Long codigoBicicleta, Long codigoSitio);
	
}