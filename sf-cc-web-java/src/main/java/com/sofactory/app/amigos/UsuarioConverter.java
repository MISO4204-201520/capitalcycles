package com.sofactory.app.amigos;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.sofactory.dtos.RespuestaUsuarioDTO;
import com.sofactory.dtos.UsuarioDTO;
 
 
@FacesConverter("usuarioConverter")
public class UsuarioConverter implements Converter {
 
	private String getUsuarioPorCodigo = "http://localhost:8080/sf-cc-gestion-usuario/rest/gestionarUsuarioService/encontrarUsuarioPorCodigo/";
	
    public Object getAsObject(FacesContext fc, UIComponent uic, String codigo) {
    	try{
			if (codigo!=null && !codigo.isEmpty()){
				String servicio = getUsuarioPorCodigo+codigo;
				Client client = ClientBuilder.newClient();
				WebTarget messages = client.target(servicio);
				RespuestaUsuarioDTO respuesta = messages.request("application/json").get(RespuestaUsuarioDTO.class);
				if (respuesta!=null){
					if (respuesta.getCodigo()==0){
						return respuesta.getUsuarios().get(0);
						
					}
				}
			}
		}catch(Exception excepcion){
			
		}
		return null;
	}
 
    public String getAsString(FacesContext fc, UIComponent uic, Object objeto) {
    	if(objeto != null){
			return String.valueOf(((UsuarioDTO) objeto).getCodigo());
		}
		else{
			return null;
		}
    }   
}     