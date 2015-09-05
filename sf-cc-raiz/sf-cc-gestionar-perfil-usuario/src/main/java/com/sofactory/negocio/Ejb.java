package com.sofactory.negocio;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.sofactory.negocio.interfaces.EjbLocal;

@Stateless
@Local({EjbLocal.class})
public class Ejb implements EjbLocal {


}
