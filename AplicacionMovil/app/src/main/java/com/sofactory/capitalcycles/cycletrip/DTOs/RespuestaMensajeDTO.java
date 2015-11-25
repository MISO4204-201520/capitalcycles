package com.sofactory.capitalcycles.cycletrip.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuisSebastian on 10/18/15.
 */
public class RespuestaMensajeDTO extends RespuestaDTO{

    private List<MensajeDTO> mensajes;

    public RespuestaMensajeDTO(){}

    public RespuestaMensajeDTO(int codigo, String mensaje) {
        super(codigo, mensaje);
        mensajes = new ArrayList<MensajeDTO>();
    }

    public List<MensajeDTO> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<MensajeDTO> mensajes) {
        this.mensajes = mensajes;
    }

}
