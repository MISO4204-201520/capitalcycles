package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.negocio.interfaces.SeguridadBeanLocal;

@Path("seguridadService")
public class SeguridadService {

	@EJB
	private SeguridadBeanLocal seguridadBeanLocal;

	@POST
	@Path("esValidoUsuario")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaSeguridadDTO esValidoUsuario(UsuarioDTO usuarioDTO) {
		RespuestaSeguridadDTO respuestaSeguridadDTO = new  RespuestaSeguridadDTO(3, "Campos Obligatorios");
		if (usuarioDTO!=null && usuarioDTO.getLogin()!=null && !usuarioDTO.getLogin().isEmpty() &&
				usuarioDTO.getCredencial()!=null && !usuarioDTO.getCredencial().isEmpty()){
			return seguridadBeanLocal.esValidoUsuario(usuarioDTO.getLogin(), usuarioDTO.getCredencial());	
		}
		
		return respuestaSeguridadDTO;
	}
}