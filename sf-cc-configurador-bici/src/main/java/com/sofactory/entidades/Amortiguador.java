package com.sofactory.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AMORTIGUADOR")
public class Amortiguador extends Suspension{

}
