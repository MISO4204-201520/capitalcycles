package com.sofactory.negocio;

import javax.ejb.Singleton;

@Singleton
public class FactoriaRedSocialBean {
	
	private static final String TWITTER = "twitter";
	private static final String FACEBOOK = "facebook";
	
	public RedSocialBean getRedSocial(String redSocial){
		if (redSocial.equalsIgnoreCase(TWITTER)){
			return new TwitterBean();
		}else if (redSocial.equalsIgnoreCase(FACEBOOK)){
			return new FacebookBean();
		}
		return  null;
	}
}