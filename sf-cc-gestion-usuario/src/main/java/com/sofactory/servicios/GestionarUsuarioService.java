package com.sofactory.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.sofactory.dtos.RespuestaRolDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.RolDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Persona;
import com.sofactory.entidades.Rol;
import com.sofactory.entidades.Usuario;
import com.sofactory.enums.Genero;
import com.sofactory.excepciones.CorreoInvalidoException;
import com.sofactory.excepciones.RegistroYaExisteException;
import com.sofactory.negocio.interfaces.RolBeanLocal;
import com.sofactory.negocio.interfaces.UsuarioBeanLocal;

@Path("gestionarUsuarioService")
public class GestionarUsuarioService {

	@EJB
	private UsuarioBeanLocal usuarioBeanLocal;

	@EJB
	private RolBeanLocal rolBeanLocal;

	@GET
	@Path("encontrarTodosUsuarios")
	@Produces("application/json")
	public RespuestaUsuarioDTO encontrarTodosUsuarios() {
		RespuestaUsuarioDTO respuestaUsuarioDTO = new RespuestaUsuarioDTO(0, "OK");
		try{
			List<Usuario> usuarios = usuarioBeanLocal.encontrarTodos(Usuario.class, "login", "ASC");
			for (Usuario u:usuarios){
				if (u instanceof Persona){
					Persona p = (Persona)u;
					UsuarioDTO usuarioDTO = new UsuarioDTO(p.getCodigo(), 
															p.getLogin(), 
															p.getNombres(), 
															p.getApellidos(),
															p.getCelular(), 
															p.getGenero()!=null?p.getGenero().name():null,
															p.getCorreo());
					List<Rol> roles = rolBeanLocal.encontrarRolesPorUsuario(u.getCodigo());
					if (roles!=null){
						for (Rol rol:roles){
							RolDTO rolDTO = new RolDTO(rol.getId(), rol.getNombre());
							usuarioDTO.getRoles().add(rolDTO);
						}
					}
					respuestaUsuarioDTO.getUsuarios().add(usuarioDTO);
				}
			}
		}catch(Exception e){
			respuestaUsuarioDTO.setCodigo(1);
			respuestaUsuarioDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		return respuestaUsuarioDTO;
	}

	@POST
	@Path("crear")
	@Produces("application/json")
	public RespuestaUsuarioDTO crear(@QueryParam("login") String login, @QueryParam("credencial") String credencial,
			@QueryParam("nombres") String nombres, @QueryParam("apellidos") String apellidos,
			@QueryParam("celular") String celular, @QueryParam("genero") String genero,
			@QueryParam("correo") String correo, @QueryParam("codigoRol") String codigoRol) {

		RespuestaUsuarioDTO respuestaUsuarioDTO = new RespuestaUsuarioDTO(0, "OK");
		if (login!=null && !login.isEmpty() && 
				credencial!=null && !credencial.isEmpty() &&
				nombres!=null && !nombres.isEmpty() &&
				apellidos!=null && !apellidos.isEmpty() &&
				codigoRol!=null && !codigoRol.isEmpty()){
			try {
				//Validar Codigo Rol
				Rol rol = rolBeanLocal.encontrarPorId(Rol.class, new Integer(codigoRol));
				if (rol!=null){
					Persona persona = new Persona();
					persona.setLogin(login);
					persona.setPassword(login);
					persona.setNombres(login);
					persona.setApellidos(login);
					persona.setRoles(new ArrayList<Rol>());
					persona.getRoles().add(rol);
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
				}else{
					respuestaUsuarioDTO.setCodigo(4);
					respuestaUsuarioDTO.setMensaje("El rol ingresado no existe en el sistema");
				}
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

	@GET
	@Path("encontrarTodosRoles")
	@Produces("application/json")
	public RespuestaRolDTO encontrarTodosRoles() {
		RespuestaRolDTO respuestaRolDTO = new RespuestaRolDTO(0, "OK");
		try{
			List<Rol> roles = rolBeanLocal.encontrarTodos(Rol.class, "id", "ASC");
			for (Rol r:roles){
				RolDTO rolDTO = new RolDTO(r.getId(), r.getNombre());
				respuestaRolDTO.getRoles().add(rolDTO);
			}
		}catch(Exception e){
			respuestaRolDTO.setCodigo(1);
			respuestaRolDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		return respuestaRolDTO;
	}
	
	@GET
	@Path("encontrarUsuarioPorId/{id}")
	@Produces("application/json")
	public RespuestaUsuarioDTO encontrarUsuarioPorId(@PathParam("id") String id) {
		RespuestaUsuarioDTO respuestaUsuarioDTO = new RespuestaUsuarioDTO(0, "OK");
		try{
			Usuario usuario = usuarioBeanLocal.encontrarPorId(Usuario.class, new Long(id));
			if (usuario!=null){
				if (usuario instanceof Persona){
					Persona p = (Persona)usuario;
					UsuarioDTO usuarioDTO = new UsuarioDTO(p.getCodigo(), 
															p.getLogin(), 
															p.getNombres(), 
															p.getApellidos(),
															p.getCelular(), 
															p.getGenero()!=null?p.getGenero().name():null,
															p.getCorreo());
					List<Rol> roles = rolBeanLocal.encontrarRolesPorUsuario(new Long(id));
					if (roles!=null){
						for (Rol rol:roles){
							RolDTO rolDTO = new RolDTO(rol.getId(), rol.getNombre());
							usuarioDTO.getRoles().add(rolDTO);
						}
					}
					respuestaUsuarioDTO.getUsuarios().add(usuarioDTO);
				}
			}else{
				respuestaUsuarioDTO.setCodigo(2);
				respuestaUsuarioDTO.setMensaje("El usuario no existe en el sistema");
			}
			
		}catch(Exception e){
			respuestaUsuarioDTO.setCodigo(1);
			respuestaUsuarioDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		return respuestaUsuarioDTO;
	}
}