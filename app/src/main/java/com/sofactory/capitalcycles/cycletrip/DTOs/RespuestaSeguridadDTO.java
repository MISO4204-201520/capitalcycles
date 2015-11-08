package com.sofactory.capitalcycles.cycletrip.DTOs;

/**
 * Created by LuisSebastian on 10/12/15.
 */
public class RespuestaSeguridadDTO extends RespuestaDTO{


    private String login;
    private String credencial;
    private String nombres;
    private String apellidos;
    private String correo;

    public RespuestaSeguridadDTO(){}

    public RespuestaSeguridadDTO(int codigo, String mensaje) {
        super(codigo, mensaje);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCredencial() {
        return credencial;
    }

    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
