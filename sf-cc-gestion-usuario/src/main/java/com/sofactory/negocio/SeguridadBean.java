package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jasypt.util.text.BasicTextEncryptor;

import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Persona;
import com.sofactory.entidades.Usuario;
import com.sofactory.enums.Estado;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.SeguridadBeanLocal;

/**
 * Session Bean implementation class SeguridadBean
 */
@Stateless
@Local({SeguridadBeanLocal.class})
public class SeguridadBean  extends GenericoBean<Usuario> implements SeguridadBeanLocal {

	private static final String LLAVE_PASSWORD = "llavePassword?.";

	private static String servicioRegistrarServicio = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/registrarServicio";
	
	@PersistenceContext(unitName="GestionUsuarioPU")
	private EntityManager em;
	
	@EJB
	private UsuarioSingletonBean usuarioSingletonBean;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}

	public RespuestaSeguridadDTO esValidoUsuario(String login, String credencial){
		RespuestaSeguridadDTO respuestaDTO = new RespuestaSeguridadDTO(0, "OK");
		List<Usuario> usuarios = encontrarPorColumna(Usuario.class, "login", login);
		if (usuarios!=null && !usuarios.isEmpty()){
			//Validar credencial
			Usuario usuario = usuarios.get(0);
			if (usuario.getEstado()==null || usuario.getEstado().equals(Estado.ACTIVO)){
				BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
				textEncryptor.setPassword(LLAVE_PASSWORD);
				
				boolean usuarioEsValido = false;
				try{
					String credencialBD = textEncryptor.decrypt(usuario.getPassword());
					String credencialUsuario = textEncryptor.decrypt(credencial);
					if (credencialUsuario.equals(credencialBD)){
						usuarioEsValido = true;
					}
				}catch(Exception exc){
				}
				if (usuarioEsValido){
					//Crear usuario en sesion
					UsuarioDTO esValidoUsuarioDTO = new UsuarioDTO();
					esValidoUsuarioDTO.setCodigo(usuario.getCodigo());
					esValidoUsuarioDTO.setLogin(usuario.getLogin());
					esValidoUsuarioDTO.setCredencial(usuario.getPassword());
					if (usuario instanceof Persona){
						esValidoUsuarioDTO.setNombres(((Persona)usuario).getNombres());
						esValidoUsuarioDTO.setApellidos(((Persona)usuario).getApellidos());
						esValidoUsuarioDTO.setCorreo(((Persona)usuario).getCorreo());
					}
					usuarioSingletonBean.getUsuariosDTO().put(esValidoUsuarioDTO.getCodigo(), esValidoUsuarioDTO);
					respuestaDTO.setCodigoUsuario(usuario.getCodigo().toString());
					respuestaDTO.setLogin(usuario.getLogin());
					respuestaDTO.setFoto(usuario.getFoto());
					if (usuario instanceof Persona){
						respuestaDTO.setNombres(((Persona)usuario).getNombres());
						respuestaDTO.setApellidos(((Persona)usuario).getApellidos());
						respuestaDTO.setCorreo(((Persona)usuario).getCorreo());
					}
					// Inicio Otorga Puntos por Fidelizacion
					
					RegistrarPuntosDTO registrarPuntosDTO = new RegistrarPuntosDTO();
					UsuarioDTO usuarioDTO = new UsuarioDTO();
					usuarioDTO.setCodigo(esValidoUsuarioDTO.getCodigo());
					registrarPuntosDTO.setCodigoUsuario(usuarioDTO.getCodigo().toString());
					registrarPuntosDTO.setServicio("autenticacionUsuario");
				
					Client client = ClientBuilder.newClient();
					WebTarget targetMensaje = client.target(servicioRegistrarServicio);
					RespuestaDTO resuDTO = targetMensaje.request("application/json").post(Entity.entity(registrarPuntosDTO, MediaType.APPLICATION_JSON),RespuestaDTO.class);		
					
					// Fin Otorga Puntos por Fidelizacion
				}else{
					respuestaDTO.setCodigo(2);
					respuestaDTO.setMensaje("La credencial del usuario es incorrecta");
				}	
			}else{
				respuestaDTO.setCodigo(1);
				respuestaDTO.setMensaje("El usuario no existe en el sistema");
			}
		}else{
			respuestaDTO.setCodigo(1);
			respuestaDTO.setMensaje("El usuario no existe en el sistema");
		}
		
		return respuestaDTO;
	}
	
	public RespuestaSeguridadDTO obtenerUsuarioSesion(Long codigoUsuario){
		RespuestaSeguridadDTO respuestaDTO = new RespuestaSeguridadDTO(0, "OK");
		UsuarioDTO usuarioSesion = usuarioSingletonBean.getUsuariosDTO().get(codigoUsuario);
		if (usuarioSesion!=null){
			respuestaDTO.setCodigoUsuario(usuarioSesion.getCodigo().toString());
			respuestaDTO.setLogin(usuarioSesion.getLogin());
			respuestaDTO.setNombres(usuarioSesion.getNombres());
			respuestaDTO.setApellidos(usuarioSesion.getApellidos());
			respuestaDTO.setCorreo(usuarioSesion.getCorreo());
		}else{
			respuestaDTO.setCodigo(1);
			respuestaDTO.setMensaje("El usuario no se encuentra en sesion");
		}
		
		return respuestaDTO;
	}
	
	public RespuestaSeguridadDTO cerrarSesion(Long codigoUsuario){
		RespuestaSeguridadDTO respuestaDTO = new RespuestaSeguridadDTO(0, "OK");
		UsuarioDTO usuarioSesion = usuarioSingletonBean.getUsuariosDTO().get(codigoUsuario);
		if (usuarioSesion!=null){
			usuarioSingletonBean.getUsuariosDTO().remove(codigoUsuario);
		}else{
			respuestaDTO.setCodigo(1);
			respuestaDTO.setMensaje("El usuario no se encuentra en sesion");
		}
		
		return respuestaDTO;
	}

	public String encriptar(String credencial) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(LLAVE_PASSWORD);
		return textEncryptor.encrypt(credencial);
	}

	public RespuestaUsuarioDTO cambiarCredencial(Usuario usuario, UsuarioDTO usuarioDTO) {
		RespuestaUsuarioDTO respuestaDTO = new RespuestaUsuarioDTO(0, "OK");
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(LLAVE_PASSWORD);
		String nuevaClave = textEncryptor.decrypt(usuarioDTO.getCredencialNueva());
		String confirmarNuevaClave = textEncryptor.decrypt(usuarioDTO.getConfirmacionCredencialNueva());
		if (nuevaClave.equals(confirmarNuevaClave)){
			usuario.setPassword(usuarioDTO.getCredencialNueva());
			em.merge(usuario);
		}else{
			respuestaDTO.setCodigo(6);
			respuestaDTO.setMensaje("La credencial nueva es diferente a la credencial de confirmacion");
		}
		
		return respuestaDTO;
	}
}