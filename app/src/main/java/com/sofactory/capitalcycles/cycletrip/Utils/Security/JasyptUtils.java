package com.sofactory.capitalcycles.cycletrip.Utils.Security;

import org.jasypt.util.text.BasicTextEncryptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by LuisSebastian on 10/11/15.
 */
public class JasyptUtils {

    private static final String LLAVE_PASSWORD = "llavePassword?.";

    public static String encryptPassword(String password) throws UnsupportedEncodingException {

        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(LLAVE_PASSWORD);
        String ePassword =textEncryptor.encrypt(password);
        return ePassword;

    }

}
