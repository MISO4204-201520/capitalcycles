package com.sofactory.servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Persona;
import com.sofactory.entidades.Rol;
import com.sofactory.entidades.Usuario;
import com.sofactory.enums.Estado;
import com.sofactory.negocio.FactoriaRedSocialBean;
import com.sofactory.negocio.UsuarioSingletonBean;
import com.sofactory.negocio.interfaces.SeguridadBeanLocal;
import com.sofactory.negocio.interfaces.UsuarioBeanLocal;

@Path("seguridadService")
public class SeguridadService {

	private static String servicioRegistrarServicio = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/registrarServicio";
	
	@EJB
	private SeguridadBeanLocal seguridadBeanLocal;

	@EJB
	private UsuarioBeanLocal usuarioBeanLocal;

	@EJB	
	private UsuarioSingletonBean usuarioSingletonBean;
	
	@EJB
	private FactoriaRedSocialBean factoriaRedSocialBean;
	
	@Context
	private HttpServletRequest request;
	 
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
	
	
	@POST
	@Path("cambiarCredencial")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaUsuarioDTO cambiarCredencial(UsuarioDTO usuarioDTO) {
		RespuestaUsuarioDTO respuestaUsuarioDTO = new RespuestaUsuarioDTO(0, "OK");
		if (usuarioDTO.getCodigo()!=null && 
				usuarioDTO.getCredencialNueva()!=null && !usuarioDTO.getCredencialNueva().isEmpty() &&
				usuarioDTO.getConfirmacionCredencialNueva()!=null && !usuarioDTO.getConfirmacionCredencialNueva().isEmpty()){
			try {
				//Validar usuario en sesion
				if (seguridadBeanLocal.obtenerUsuarioSesion(usuarioDTO.getCodigo())!=null &&
						seguridadBeanLocal.obtenerUsuarioSesion(usuarioDTO.getCodigo()).getCodigo()==0){
					//Validar si el usuario existe en el sistema
					Usuario usuario = usuarioBeanLocal.encontrarPorId(Usuario.class, usuarioDTO.getCodigo());
					if (usuario!=null && (usuario.getEstado()==null || usuario.getEstado().equals(Estado.ACTIVO))){
						usuarioDTO.setLogin(usuario.getLogin());
						respuestaUsuarioDTO = seguridadBeanLocal.cambiarCredencial(usuario, usuarioDTO);
					}else{
						respuestaUsuarioDTO.setCodigo(4);
						respuestaUsuarioDTO.setMensaje("El usuario no existe en el sistema");
					}
				}else{
					respuestaUsuarioDTO.setCodigo(7);
					respuestaUsuarioDTO.setMensaje("El usuario no se encuentra en sesion");
				}
			} catch(IllegalArgumentException e){
				respuestaUsuarioDTO.setCodigo(3);
				respuestaUsuarioDTO.setMensaje("Campos con formatos invalidos");
			} catch(Exception e){
				respuestaUsuarioDTO.setCodigo(5);
				respuestaUsuarioDTO.setMensaje("Hubo un error en el sistema");
			} 
		}else{
			respuestaUsuarioDTO.setCodigo(1);
			respuestaUsuarioDTO.setMensaje("Faltan Campos Obligatorios");
		}

		return respuestaUsuarioDTO;
	}
	
	@POST
	@Path("obtenerUsuarioSesion")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaSeguridadDTO obtenerUsuarioSesion(UsuarioDTO usuarioDTO){
		return seguridadBeanLocal.obtenerUsuarioSesion(usuarioDTO.getCodigo());
	}
	
	@POST
	@Path("cerrarSesion")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaSeguridadDTO cerrarSesion(UsuarioDTO usuarioDTO){
		request.getSession().invalidate();
		return seguridadBeanLocal.cerrarSesion(usuarioDTO.getCodigo());
	}
	
	@POST
	@Path("encriptar")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaSeguridadDTO encriptar(UsuarioDTO usuarioDTO){
		RespuestaSeguridadDTO respuestaSeguridadDTO = new RespuestaSeguridadDTO(0, "OK");
		if (usuarioDTO.getCredencial() != null && !usuarioDTO.getCredencial().isEmpty()){
			respuestaSeguridadDTO.setCredencial(seguridadBeanLocal.encriptar(usuarioDTO.getCredencial()));
		}else{
			respuestaSeguridadDTO.setCodigo(1);
			respuestaSeguridadDTO.setMensaje("Faltan Campos Obligatorios");
		}
		return respuestaSeguridadDTO;
	}
	
	@GET
	@Path("redSocialObtenerUrl/{redSocial}")
	@Consumes(MediaType.TEXT_PLAIN)
	public String redSocialObtenerUrl(@PathParam("redSocial") String redSocial) {
		return factoriaRedSocialBean.getRedSocial(redSocial).obtenerUrl();
	}
	
	@POST
	@Path("guardarUsuarioRedSocial")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaSeguridadDTO guardarUsuarioRedSocial(UsuarioDTO usuarioDTO) {
		RespuestaSeguridadDTO respuestaSeguridadDTO = new  RespuestaSeguridadDTO(0, "OK");
		UsuarioDTO uDTO = factoriaRedSocialBean.getRedSocial(usuarioDTO.getRedSocial()).obtenerUsuarioRedSocial(usuarioDTO.getVerificador());
		if (uDTO!=null){
			if (!usuarioBeanLocal.existeUsuarioRedSocial(uDTO.getNombres(), usuarioDTO.getRedSocial())){
				//Guardar Usuario Red Social
				Rol rol = new Rol();
				rol.setId(2);
				List<Rol> roles = new ArrayList<Rol>();
				roles.add(rol);
				Persona usuario = new Persona();
				usuario.setNombres(uDTO.getNombres());
				usuario.setApellidos(uDTO.getApellidos());
				usuario.setUserId(uDTO.getUserId());
				usuario.setRedSocial(usuarioDTO.getRedSocial());
				usuario.setRoles(roles);
				usuario = (Persona) usuarioBeanLocal.insertarOActualizar(usuario);
				uDTO.setCodigo(usuario.getCodigo());
				respuestaSeguridadDTO.setCodigoUsuario(usuario.getCodigo().toString());
				respuestaSeguridadDTO.setNombres(usuario.getNombres());
				respuestaSeguridadDTO.setApellidos(usuario.getApellidos());
			}else{
				Persona usuario = (Persona)usuarioBeanLocal.encontrarPorRedSocial(usuarioDTO.getRedSocial(),uDTO.getNombres());
				uDTO.setCodigo(usuario.getCodigo());
				respuestaSeguridadDTO.setCodigoUsuario(usuario.getCodigo().toString());
				respuestaSeguridadDTO.setNombres(usuario.getNombres());
				respuestaSeguridadDTO.setApellidos(usuario.getApellidos());
			}
			usuarioSingletonBean.getUsuariosDTO().put(uDTO.getCodigo(), uDTO);
			
			// Inicio Otorga Puntos por Fidelizacion
			RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
			registrarPuntosDTO.setCodigoUsuario(respuestaSeguridadDTO.getCodigoUsuario());
			registrarPuntosDTO.setServicio("autenticacionUsuario");
			Client client = ClientBuilder.newClient();
			WebTarget targetMensaje = client.target(servicioRegistrarServicio);
			targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);		
			// Fin Otorga Puntos por Fidelizacion
		}
		
		return respuestaSeguridadDTO;
	}
	
	@GET
	@Path("redSocialCerrarSesion")
	@Consumes(MediaType.TEXT_PLAIN)
	public String redSocialCerrarSesion(@QueryParam("redesSociales") String redesSociales) {
		if (redesSociales!=null && !redesSociales.isEmpty()){
			String[] splitRed = redesSociales.split(",");
			for (String redSocial:splitRed){
				factoriaRedSocialBean.getRedSocial(redSocial).cerrarSession();	
			}
		}
		return "OK";
	}
}