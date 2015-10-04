package com.sofactory.servicios;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.entidades.Persona;
import com.sofactory.entidades.Usuario;
import com.sofactory.enums.Genero;
import com.sofactory.excepciones.CorreoInvalidoException;
import com.sofactory.excepciones.RegistroYaExisteException;
import com.sofactory.negocio.interfaces.UsuarioBeanLocal;

@Path("gestionarUsuarioService")
public class GestionarUsuarioService {

	@EJB
	private UsuarioBeanLocal usuarioBeanLocal;

	@GET
	@Path("encontrarTodos")
	@Produces("application/json")
	public List<?> encontrarTodos() {
		List<Usuario> usuarios = usuarioBeanLocal.encontrarTodos(Usuario.class);
		return usuarios;
	}

	@POST
	@Path("crear")
	@Produces("application/json")
	public RespuestaUsuarioDTO crear(@QueryParam("login") String login, @QueryParam("credencial") String credencial,
			@QueryParam("nombres") String nombres, @QueryParam("apellidos") String apellidos,
			@QueryParam("celular") String celular, @QueryParam("genero") String genero,
			@QueryParam("correo") String correo) {

		RespuestaUsuarioDTO respuestaUsuarioDTO = new RespuestaUsuarioDTO(0, "OK");
		if (login!=null && !login.isEmpty() && 
				credencial!=null && !credencial.isEmpty() &&
				nombres!=null && !nombres.isEmpty() &&
				apellidos!=null && !apellidos.isEmpty()){
			try {
				Persona persona = new Persona();
				persona.setLogin(login);
				persona.setPassword(login);
				persona.setNombres(login);
				persona.setApellidos(login);
				if (celular!=null && !celular.isEmpty()){
					persona.setCelular(login);	
				}
				if (genero!=null && !genero.isEmpty()){
					persona.setGenero(Genero.valueOf(genero));	
				}
				if (correo!=null && !correo.isEmpty()){
					persona.setCorreo(correo);	
				}
				usuarioBeanLocal.insertar(persona);
			} catch (RegistroYaExisteException e) {
				respuestaUsuarioDTO.setCodigo(2);
				respuestaUsuarioDTO.setMensaje(e.getMensaje());
			} catch(CorreoInvalidoException e){
				respuestaUsuarioDTO.setCodigo(3);
				respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
			} catch(IllegalArgumentException e){
				respuestaUsuarioDTO.setCodigo(3);
				respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
			} 
		}else{
			respuestaUsuarioDTO.setCodigo(1);
			respuestaUsuarioDTO.setMensaje("Faltan Campos Obligatorios");
		}

		return respuestaUsuarioDTO;
	}

}
