package com.sofactory.negocio;

import com.sofactory.dtos.UsuarioDTO;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.User;
import facebook4j.auth.AccessToken;

public class FacebookBean implements RedSocialBean{
	private static final String APP_ID = "1633916530190069";
	private static final String APP_SECRET = "fff20b76bb5ee15d94a19eebb599779d";
	private static final String REDIRECT_URI = "http://localhost:8080/sf-cc-web-java/administracion/paginaBlanco.xhtml";
	
	@Override
	public String obtenerUrl(){
		String urlFacebook=null;
		try{
			Facebook  facebook = FacebookFactory.getSingleton();
			facebook.setOAuthAppId(APP_ID, APP_SECRET);
			urlFacebook = facebook.getOAuthAuthorizationURL(REDIRECT_URI);
		}catch(Exception exc){
			try{
				Facebook  facebook = FacebookFactory.getSingleton();
				urlFacebook = facebook.getOAuthAuthorizationURL(REDIRECT_URI);
			}catch(Exception exc1){
			}
		}
		return urlFacebook;
	}

	@Override
	public UsuarioDTO obtenerUsuarioRedSocial(String codigo) {
		UsuarioDTO usuarioDTO = null;
		try {
			Facebook  facebook = FacebookFactory.getSingleton();
			facebook.getOAuthAccessToken(codigo);
			User user = facebook.getMe();
			if (user != null){
				String[] splitNombre = user.getName().split(" ");
				usuarioDTO = new UsuarioDTO();
				if (splitNombre.length>1){
					usuarioDTO.setNombres(splitNombre[0]);
					usuarioDTO.setApellidos(splitNombre[1]);	
				}
				usuarioDTO.setRedSocial("facebook");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuarioDTO;
	}

	@Override
	public void cerrarSession() {
		try{
			Facebook  facebook = FacebookFactory.getSingleton();
			facebook.setOAuthAccessToken(null);
		}catch(Exception exc){
		}
	}

	@Override
	public String compartir(String mensaje, String usuarioToken) {
		String esCorrecto = "OK";
		try {
			Facebook facebook = FacebookFactory.getSingleton();
			AccessToken at = new AccessToken(usuarioToken);
			facebook.setOAuthAccessToken(at);
			facebook.setOAuthPermissions("email,user_posts");
			facebook.postStatusMessage(mensaje.length()<=140?mensaje:mensaje.substring(0,140));
		} catch (Exception e) {
			esCorrecto = "BAD";
			e.printStackTrace();
		}
		return esCorrecto;
	}
}