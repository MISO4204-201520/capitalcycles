package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Usuario;
import com.sofactory.enums.Estado;
import com.sofactory.negocio.interfaces.SeguridadBeanLocal;
import com.sofactory.negocio.interfaces.UsuarioBeanLocal;

@Path("seguridadService")
public class SeguridadService {

	@EJB
	private SeguridadBeanLocal seguridadBeanLocal;

	@EJB
	private UsuarioBeanLocal usuarioBeanLocal;
	
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
				usuarioDTO.getCredencial()!=null && !usuarioDTO.getCredencial().isEmpty() &&
				usuarioDTO.getCredencialNueva()!=null && !usuarioDTO.getCredencialNueva().isEmpty() &&
				usuarioDTO.getConfirmacionCredencialNueva()!=null && !usuarioDTO.getConfirmacionCredencialNueva().isEmpty()){
			try {
				//Validar si el usuario existe en el sistema
				Usuario usuario = usuarioBeanLocal.encontrarPorId(Usuario.class, usuarioDTO.getCodigo());
				if (usuario!=null && (usuario.getEstado()==null || usuario.getEstado().equals(Estado.ACTIVO))){
					usuarioDTO.setLogin(usuario.getLogin());
					RespuestaSeguridadDTO usuarioValidoDTO = esValidoUsuario(usuarioDTO);
					if (usuarioValidoDTO!=null){
						if (usuarioValidoDTO.getCodigo()==0){
							if (usuarioDTO.getCredencialNueva().equals(usuarioDTO.getConfirmacionCredencialNueva())){
								usuario.setPassword(usuarioDTO.getCredencialNueva());
								usuarioBeanLocal.insertarOActualizar(usuario);
							}else{
								respuestaUsuarioDTO.setCodigo(6);
								respuestaUsuarioDTO.setMensaje("La credencial nueva es diferente a la credencial de confirmacion");
							}
						}else{
							respuestaUsuarioDTO.setCodigo(2);
							respuestaUsuarioDTO.setMensaje("La credencial del usuario es invalida");
						}
					}
				}else{
					respuestaUsuarioDTO.setCodigo(4);
					respuestaUsuarioDTO.setMensaje("El usuario no existe en el sistema");
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
}