package com.sofactory.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MARCO")
public class Marco extends Parte{

}
