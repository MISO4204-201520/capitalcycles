package com.sofactory.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PEDAL")
public class Pedal extends Parte{

}
