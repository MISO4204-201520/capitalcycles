package com.sofactory.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.sofactory.dtos.RespuestaRolDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.RolDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Persona;
import com.sofactory.entidades.Rol;
import com.sofactory.entidades.Usuario;
import com.sofactory.enums.Genero;
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
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaUsuarioDTO crear(UsuarioDTO usuarioDTO) {

		RespuestaUsuarioDTO respuestaUsuarioDTO = new RespuestaUsuarioDTO(0, "OK");
		if (usuarioDTO.getLogin()!=null && !usuarioDTO.getLogin().isEmpty() && 
				usuarioDTO.getCredencial()!=null && !usuarioDTO.getCredencial().isEmpty() &&
				usuarioDTO.getNombres()!=null && !usuarioDTO.getNombres().isEmpty() &&
				usuarioDTO.getApellidos()!=null && !usuarioDTO.getApellidos().isEmpty() &&
				usuarioDTO.getRoles()!=null && !usuarioDTO.getRoles().isEmpty()){
			try {
				//Validar Codigo Rol
				boolean estanRoles=true;
				List<Rol> roles = new ArrayList<>();
				for(RolDTO r: usuarioDTO.getRoles()){
					Rol rol = rolBeanLocal.encontrarPorId(Rol.class, r.getId());
					roles.add(rol);
					if (rol==null){
						estanRoles = false;
						break;
					}
				}
				
				if (estanRoles){
					Persona persona = new Persona();
					persona.setLogin(usuarioDTO.getLogin());
					persona.setPassword(usuarioDTO.getCredencial());
					persona.setNombres(usuarioDTO.getNombres());
					persona.setApellidos(usuarioDTO.getApellidos());
					persona.setRoles(roles);
					if (usuarioDTO.getCelular()!=null && !usuarioDTO.getCelular().isEmpty()){
						persona.setCelular(usuarioDTO.getCelular());	
					}
					if (usuarioDTO.getGenero()!=null && !usuarioDTO.getGenero().isEmpty()){
						persona.setGenero(Genero.valueOf(usuarioDTO.getGenero()));	
					}
					if (usuarioDTO.getCorreo()!=null && !usuarioDTO.getCorreo().isEmpty()){
						persona.setCorreo(usuarioDTO.getCorreo());	
					}
					usuarioBeanLocal.insertar(persona);	
				}else{
					respuestaUsuarioDTO.setCodigo(4);
					respuestaUsuarioDTO.setMensaje("El rol o roles ingresados no existen en el sistema");
				}
			} catch (RegistroYaExisteException e) {
				respuestaUsuarioDTO.setCodigo(2);
				respuestaUsuarioDTO.setMensaje(e.getMensaje());
			} catch(ConstraintViolationException e){
				respuestaUsuarioDTO.setCodigo(3);
				respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
			} catch(IllegalArgumentException e){
				respuestaUsuarioDTO.setCodigo(3);
				respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
			} catch(Exception e){
				if (e.getCause()!=null && e.getCause() instanceof ConstraintViolationException){
					respuestaUsuarioDTO.setCodigo(3);
					respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
				}else{
					respuestaUsuarioDTO.setCodigo(5);
					respuestaUsuarioDTO.setMensaje("Hubo un error en el sistema");
				}
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
	@Path("encontrarUsuarioPorCodigo/{codigo}")
	@Produces("application/json")
	public RespuestaUsuarioDTO encontrarUsuarioPorCodigo(@PathParam("codigo") String codigo) {
		RespuestaUsuarioDTO respuestaUsuarioDTO = new RespuestaUsuarioDTO(0, "OK");
		try{
			Usuario usuario = usuarioBeanLocal.encontrarPorId(Usuario.class, new Long(codigo));
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
					List<Rol> roles = rolBeanLocal.encontrarRolesPorUsuario(new Long(codigo));
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
	
	@GET
	@Path("encontrarRolPorId/{id}")
	@Produces("application/json")
	public RespuestaRolDTO encontrarRolPorId(@PathParam("id") String id) {
		RespuestaRolDTO respuestaRolDTO = new RespuestaRolDTO(0, "OK");
		try{
			Rol rol = rolBeanLocal.encontrarPorId(Rol.class, new Integer(id));
			if (rol!=null){
				RolDTO rolDTO = new RolDTO(rol.getId(), rol.getNombre());
				respuestaRolDTO.getRoles().add(rolDTO);
			}else{
				respuestaRolDTO.setCodigo(2);
				respuestaRolDTO.setMensaje("El rol no existe en el sistema");
			}
			
		}catch(Exception e){
			respuestaRolDTO.setCodigo(1);
			respuestaRolDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		return respuestaRolDTO;
	}
	
	@PUT
	@Path("actualizar")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaUsuarioDTO actualizar(UsuarioDTO usuarioDTO) {

		RespuestaUsuarioDTO respuestaUsuarioDTO = new RespuestaUsuarioDTO(0, "OK");
		if (usuarioDTO.getCodigo()!=null &&
				usuarioDTO.getLogin()!=null && !usuarioDTO.getLogin().isEmpty() &&
				usuarioDTO.getNombres()!=null && !usuarioDTO.getNombres().isEmpty() &&
				usuarioDTO.getApellidos()!=null && !usuarioDTO.getApellidos().isEmpty() &&
				usuarioDTO.getRoles()!=null && !usuarioDTO.getRoles().isEmpty()){
			try {
				//Validar si el usuario existe en el sistema
				Usuario usuarioActualizar = usuarioBeanLocal.encontrarPorId(Usuario.class, usuarioDTO.getCodigo());
				if (usuarioActualizar!=null){
					//Validar si el login nuevo es unico en el sistema
					if (usuarioBeanLocal.encontrarPorLogin(usuarioDTO.getCodigo(),usuarioDTO.getLogin())==null){
						//Validar Codigo Rol
						boolean estanRoles=true;
						List<Rol> roles = new ArrayList<>();
						for(RolDTO r: usuarioDTO.getRoles()){
							Rol rol = rolBeanLocal.encontrarPorId(Rol.class, r.getId());
							roles.add(rol);
							if (rol==null){
								estanRoles = false;
								break;
							}
						}
						
						if (estanRoles){
							if (usuarioActualizar instanceof Persona){
								Persona persona = (Persona)usuarioActualizar;
								persona.setLogin(usuarioDTO.getLogin());
								persona.setNombres(usuarioDTO.getNombres());
								persona.setApellidos(usuarioDTO.getApellidos());
								persona.setRoles(roles);	
								if (usuarioDTO.getCelular()!=null && !usuarioDTO.getCelular().isEmpty()){
									persona.setCelular(usuarioDTO.getCelular());	
								}
								if (usuarioDTO.getGenero()!=null && !usuarioDTO.getGenero().isEmpty()){
									persona.setGenero(Genero.valueOf(usuarioDTO.getGenero()));	
								}
								if (usuarioDTO.getCorreo()!=null && !usuarioDTO.getCorreo().isEmpty()){
									persona.setCorreo(usuarioDTO.getCorreo());	
								}
								usuarioBeanLocal.insertarOActualizar(persona);
								UsuarioDTO usuarioActDTO = new UsuarioDTO(persona.getCodigo(), 
																		persona.getLogin(), 
																		persona.getNombres(), 
																		persona.getApellidos(),
																		persona.getCelular(), 
																		persona.getGenero()!=null?persona.getGenero().name():null,
																		persona.getCorreo());
								List<RolDTO> rolesDTO = new ArrayList<RolDTO>();
								for(Rol r:persona.getRoles()){
									RolDTO rolDTO = new RolDTO(r.getId(), r.getNombre());
									rolesDTO.add(rolDTO);
								}
								usuarioActDTO.setRoles(rolesDTO);
								respuestaUsuarioDTO.getUsuarios().add(usuarioActDTO);
								
							}
						}else{
							respuestaUsuarioDTO.setCodigo(3);
							respuestaUsuarioDTO.setMensaje("El rol o roles ingresados no existen en el sistema");
						}
					}else{
						respuestaUsuarioDTO.setCodigo(5);
						respuestaUsuarioDTO.setMensaje("El login ya existe en el sistema");
					}
				}else{
					respuestaUsuarioDTO.setCodigo(6);
					respuestaUsuarioDTO.setMensaje("El usuario no existe en el sistema");
				}
			} catch(ConstraintViolationException e){
				respuestaUsuarioDTO.setCodigo(2);
				respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
			} catch(IllegalArgumentException e){
				respuestaUsuarioDTO.setCodigo(2);
				respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
			} catch(Exception e){
				if (e.getCause()!=null && e.getCause().getCause()!=null && e.getCause().getCause().getCause()!=null
						&& e.getCause().getCause().getCause() instanceof ConstraintViolationException){
					respuestaUsuarioDTO.setCodigo(2);
					respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
				}else{
					respuestaUsuarioDTO.setCodigo(4);
					respuestaUsuarioDTO.setMensaje("Hubo un error en el sistema");
				}
			} 
		}else{
			respuestaUsuarioDTO.setCodigo(1);
			respuestaUsuarioDTO.setMensaje("Faltan Campos Obligatorios");
		}

		return respuestaUsuarioDTO;
	}
	
	@POST
	@Path("crearRol")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaRolDTO crear(RolDTO rolDTO) {

		RespuestaRolDTO respuestaRolDTO = new RespuestaRolDTO(0, "OK");
		if (rolDTO.getNombre()!=null && !rolDTO.getNombre().isEmpty()){
			try {
				Rol rol = new Rol();
				rol.setNombre(rolDTO.getNombre());
				rolBeanLocal.insertar(rol);	
			} catch (RegistroYaExisteException e) {
				respuestaRolDTO.setCodigo(2);
				respuestaRolDTO.setMensaje(e.getMensaje());
			} catch(ConstraintViolationException e){
				respuestaRolDTO.setCodigo(3);
				respuestaRolDTO.setMensaje("Campos con formatos invalidos");
			} catch(IllegalArgumentException e){
				respuestaRolDTO.setCodigo(3);
				respuestaRolDTO.setMensaje("Campos con formatos invalidos");
			} catch(Exception e){
				if (e.getCause()!=null && e.getCause() instanceof ConstraintViolationException){
					respuestaRolDTO.setCodigo(3);
					respuestaRolDTO.setMensaje("Campos con formatos invalidos");
				}else{
					respuestaRolDTO.setCodigo(5);
					respuestaRolDTO.setMensaje("Hubo un error en el sistema");
				}
			} 
		}else{
			respuestaRolDTO.setCodigo(1);
			respuestaRolDTO.setMensaje("Faltan Campos Obligatorios");
		}

		return respuestaRolDTO;
	}
	
	@PUT
	@Path("actualizarRol")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaRolDTO actualizarRol(RolDTO rolDTO) {

		RespuestaRolDTO respuestaRolDTO = new RespuestaRolDTO(0, "OK");
		if (rolDTO.getNombre()!=null && !rolDTO.getNombre().isEmpty()){
			try {
				//Validar si el registro ya existe en el sistema
				Rol rolActualizar = rolBeanLocal.encontrarPorId(Rol.class, rolDTO.getId());
				if (rolActualizar!=null){
					//Validar si el login nuevo es unico en el sistema
					if (rolBeanLocal.encontrarPorNombre(rolDTO.getId(), rolDTO.getNombre())==null){
						Rol rol = rolActualizar;
						rol.setNombre(rolDTO.getNombre());
						rolBeanLocal.insertarOActualizar(rol);
						RolDTO rolActDTO = new RolDTO(rol.getId(), 
																rol.getNombre());
						respuestaRolDTO.getRoles().add(rolActDTO);
					}else{
						respuestaRolDTO.setCodigo(5);
						respuestaRolDTO.setMensaje("El rol ya existe en el sistema");
					}
				}else{
					respuestaRolDTO.setCodigo(6);
					respuestaRolDTO.setMensaje("El rol no existe en el sistema");
				}
			} catch(ConstraintViolationException e){
				respuestaRolDTO.setCodigo(2);
				respuestaRolDTO.setMensaje("Campos con formatos invalidos");
			} catch(IllegalArgumentException e){
				respuestaRolDTO.setCodigo(2);
				respuestaRolDTO.setMensaje("Campos con formatos invalidos");
			} catch(Exception e){
				if (e.getCause()!=null && e.getCause().getCause()!=null && e.getCause().getCause().getCause()!=null
						&& e.getCause().getCause().getCause() instanceof ConstraintViolationException){
					respuestaRolDTO.setCodigo(2);
					respuestaRolDTO.setMensaje("Campos con formatos invalidos");
				}else{
					respuestaRolDTO.setCodigo(4);
					respuestaRolDTO.setMensaje("Hubo un error en el sistema");
				}
			} 
		}else{
			respuestaRolDTO.setCodigo(1);
			respuestaRolDTO.setMensaje("Faltan Campos Obligatorios");
		}

		return respuestaRolDTO;
	}
}