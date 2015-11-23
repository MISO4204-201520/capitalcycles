package com.sofactory.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PARRILLA")
public class Parrilla extends Parte{

}
