package com.sofactory.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CANASTA")
public class Canasta extends Parte{

}
