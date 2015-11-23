package com.sofactory.capitalcycles.cycletrip.DTOs;

/**
 * Created by LuisSebastian on 10/18/15.
 */
public class PuntosUsuarioDTO extends RespuestaDTO{

    private Integer puntos;

   // private String codigoUsuario;

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }
}