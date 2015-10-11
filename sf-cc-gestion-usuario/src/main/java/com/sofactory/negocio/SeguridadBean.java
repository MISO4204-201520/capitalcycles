package com.sofactory.negocio;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jasypt.util.text.BasicTextEncryptor;

import com.sofactory.dtos.RespuestaSeguridadDTO;
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

	@PersistenceContext(unitName="GestionUsuarioPU")
	private EntityManager em;
	
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
					respuestaDTO.setLogin(usuario.getLogin());
					respuestaDTO.setCredencial(usuario.getPassword());
					if (usuario instanceof Persona){
						respuestaDTO.setNombres(((Persona)usuario).getNombres());
						respuestaDTO.setApellidos(((Persona)usuario).getApellidos());
					}
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
	
//	public static void main(String[] args) throws UnsupportedEncodingException{
//		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
//		textEncryptor.setPassword(LLAVE_PASSWORD);
//		System.out.println(textEncryptor.encrypt("admin"));
//		
//		System.out.println(URLEncoder.encode("gMxyt0elu9HnM1C1UQ/WLQ==","UTF-8"));
//	}
}
