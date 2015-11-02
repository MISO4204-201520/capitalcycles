package com.sofactory.negocio.interfaces;

import com.sofactory.dtos.RespuestaSitioDTO;
import com.sofactory.negocio.general.MetodosGenerales;

public interface SitioBeanLocal extends MetodosGenerales<Object>{
	RespuestaSitioDTO encontrarSitios(String lat, String lng, String radio, String sitio) throws Exception;
}
