package com.sofactory.capitalcycles.cycletrip.DTOs;

/**
 * Created by LuisSebastian on 11/10/15.
 */
public class AmigoDTO {

    private Long id;
    private Long codUsuario;
    private Long codAmigo;

    public AmigoDTO(){}

    public AmigoDTO(Long id, Long codUsuario, Long codAmigo ) {
        super();
        this.id=id;
        this.codUsuario = codUsuario;
        this.codAmigo = codAmigo;
    }

    public Long getId() {
        return id;
    }

    public Long getCodUsuario() {
        return codUsuario;
    }

    public Long getCodAmigo() {
        return codAmigo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodUsuario(Long codUsuario) {
        this.codUsuario = codUsuario;
    }

    public void setCodAmigo(Long codAmigo) {
        this.codAmigo = codAmigo;
    }


}
