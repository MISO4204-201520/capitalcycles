package com.sofactory.capitalcycles.cycletrip.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuisSebastian on 11/10/15.
 */
public class RespuestaAmigoDTO extends RespuestaDTO{

    private List<AmigoDTO> amigos;

    public RespuestaAmigoDTO(){}

    public RespuestaAmigoDTO(int codigo, String mensaje) {
        super(codigo, mensaje);
        amigos = new ArrayList<AmigoDTO>();
    }

    public List<AmigoDTO> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<AmigoDTO> amigos) {
        this.amigos = amigos;
    }
}
