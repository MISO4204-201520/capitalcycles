package com.sofactory.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CADENA_ANTIROBO")
public class CadenaAntirobo extends Parte{

}
