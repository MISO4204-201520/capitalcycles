package com.sofactory.entidades;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DISCO")
public class Disco extends Freno{

}
