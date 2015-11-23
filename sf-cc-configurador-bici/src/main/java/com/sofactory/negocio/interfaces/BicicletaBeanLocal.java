package com.sofactory.negocio.interfaces;

import java.util.List;

import com.sofactory.dtos.ParteDTO;
import com.sofactory.entidades.Bicicleta;
import com.sofactory.entidades.ListadoPartes;
import com.sofactory.negocio.general.MetodosGenerales;

public interface BicicletaBeanLocal extends MetodosGenerales<Bicicleta>{
	List<ListadoPartes> getListadoPartes(String[] listadoPartes);
	void construirBicicleta(List<ParteDTO> partes);
	List<Bicicleta> encontrarConfiguracionesPorUsuario(Long codigoUsuario);	
}
