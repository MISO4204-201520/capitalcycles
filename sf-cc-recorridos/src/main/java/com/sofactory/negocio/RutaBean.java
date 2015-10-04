package com.sofactory.negocio;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.sofactory.entidades.Posicion;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.RutaBeanLocal;

@Stateless
@Local({RutaBeanLocal.class})
public class RutaBean extends GenericoBean<Posicion> implements RutaBeanLocal  {
	
}
