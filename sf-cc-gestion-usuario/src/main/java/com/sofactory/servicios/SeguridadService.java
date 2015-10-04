package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.negocio.interfaces.SeguridadBeanLocal;

@Path("seguridadService")
public class SeguridadService {

	@EJB
	private SeguridadBeanLocal seguridadBeanLocal;

	@POST
	@Path("esValidoUsuario/{login}/{credencial}")
	@Produces("application/json")
	public RespuestaSeguridadDTO esValidoUsuario(@PathParam("login") String login, @PathParam("credencial") String credencial) {
		return seguridadBeanLocal.esValidoUsuario(login, credencial);
	}
}
