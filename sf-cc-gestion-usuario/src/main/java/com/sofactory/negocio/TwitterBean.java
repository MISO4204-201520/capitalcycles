package com.sofactory.negocio;

import com.sofactory.dtos.UsuarioDTO;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterBean implements RedSocialBean {
	private static final String CONSUMER_T_KEY = "4YW7sxP7IZrRDb2egJhkXKAEi";
	private static final String CONSUMER_T_SECRET = "NkFMK9Pm88YxTerXBV4Lgzndjcb0rzzszQdpXjdqp5BLsejYBL";
	
	@Override
	public String obtenerUrl(){
		String urlTwitter=null;
		try{
			Twitter twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer(CONSUMER_T_KEY, CONSUMER_T_SECRET);
			RequestToken requestToken = twitter.getOAuthRequestToken();
			urlTwitter = requestToken.getAuthenticationURL();
		}catch(Exception exc){
			try{
				Twitter twitter = TwitterFactory.getSingleton();
				RequestToken requestToken = twitter.getOAuthRequestToken();
				urlTwitter = requestToken.getAuthenticationURL();
			}catch(Exception exc1){
			}
		}
		return urlTwitter;
	}

	@Override
	public UsuarioDTO obtenerUsuarioRedSocial(String verificador) {
		UsuarioDTO usuarioDTO = null;
		try {
			Twitter twitter = TwitterFactory.getSingleton();
			AccessToken accessToken = twitter.getOAuthAccessToken(verificador);
			User user = twitter.showUser(accessToken.getScreenName());
			if (user != null){
				usuarioDTO = new UsuarioDTO();
				usuarioDTO.setLogin(accessToken.getScreenName());
				usuarioDTO.setUserId(user.getId());
				usuarioDTO.setNombres(user.getName());
				usuarioDTO.setRedSocial("twitter");
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return usuarioDTO;
	}

	@Override
	public void cerrarSession() {
		try{
			Twitter twitter = TwitterFactory.getSingleton();
			twitter.setOAuthAccessToken(null);
		}catch(Exception exc){
		}
	}
	
	@Override
	public String compartir(String mensaje, String usuarioToken) {
		String esCorrecto = "OK";
		try {
			Twitter twitter = TwitterFactory.getSingleton();
			twitter.updateStatus(mensaje.length()<=140?mensaje:mensaje.substring(0,140));
		} catch (Exception e) {
			esCorrecto = "BAD";
			e.printStackTrace();
		}
		return esCorrecto;
	}

}