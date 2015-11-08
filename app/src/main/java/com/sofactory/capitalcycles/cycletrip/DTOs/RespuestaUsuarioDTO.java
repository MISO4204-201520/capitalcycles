package com.sofactory.capitalcycles.cycletrip.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuisSebastian on 10/12/15.
 */
public class RespuestaUsuarioDTO extends RespuestaDTO{

    private List<UsuarioDTO> usuarios;

    public RespuestaUsuarioDTO (){}

    public RespuestaUsuarioDTO(int codigo, String mensaje) {
        super(codigo, mensaje);
        usuarios = new ArrayList<UsuarioDTO>();
    }

    public List<UsuarioDTO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioDTO> usuarios) {
        this.usuarios = usuarios;
    }
}

