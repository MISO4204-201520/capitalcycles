package com.sofactory.capitalcycles.cycletrip.DTOs;

/**
 * Created by LuisSebastian on 10/17/15.
 */

    public class RequestDTO {

        protected String codigoUsuario;

        public RequestDTO() {
            super();
        }

        public RequestDTO(String codigoUsuario) {
            super();
            this.codigoUsuario = codigoUsuario;
        }

        public String getCodigoUsuario() {
            return codigoUsuario;
        }

        public void setCodigoUsuario(String codigoUsuario) {
            this.codigoUsuario = codigoUsuario;
        }
    }


