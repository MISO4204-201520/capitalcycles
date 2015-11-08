package com.sofactory.capitalcycles.cycletrip.DTOs;

/**
 * Created by LuisSebastian on 10/12/15.
 */
public class RolDTO {
    private Integer id;
    private String nombre;

    public RolDTO(){}

    public RolDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
