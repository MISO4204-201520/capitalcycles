package com.sofactory.app.configurador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.BicicletaDTO;
import com.sofactory.dtos.RespuestaBicicletaDTO;

@ManagedBean
@ViewScoped
public class ConsultarConfiguradorBiciManagedBean implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;
	
	private String getEncontrarConfiguracionesPorUsuario = "http://localhost:8080/sf-cc-configurador-bici/rest/gestionarCBService/encontrarConfiguracionesPorUsuario/";
	
	private List<BicicletaDTO> bicicletaDTOs;

	private BicicletaDTO bicicletaSeleccionada;
		
	@PostConstruct
	private void iniciar(){
		bicicletaDTOs = new ArrayList<BicicletaDTO>();
		if (usuarioManagedBean!=null && usuarioManagedBean.getUsuarioDTO()!=null && usuarioManagedBean.getUsuarioDTO().getCodigo()!=null){
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(getEncontrarConfiguracionesPorUsuario+usuarioManagedBean.getUsuarioDTO().getCodigo());
			RespuestaBicicletaDTO respuesta = messages.request("application/json").get(RespuestaBicicletaDTO.class);
			if (respuesta!=null){
				if (respuesta.getCodigo()==0){
					bicicletaDTOs = respuesta.getBicicletaDTOs();
				}else{
					FacesContext.getCurrentInstance().addMessage(null, 
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR, 
									null, 
									respuesta.getMensaje()));
				}
			}
		}
	}

	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public List<BicicletaDTO> getBicicletaDTOs() {
		return bicicletaDTOs;
	}

	public void setBicicletaDTOs(List<BicicletaDTO> bicicletaDTOs) {
		this.bicicletaDTOs = bicicletaDTOs;
	}

	public BicicletaDTO getBicicletaSeleccionada() {
		return bicicletaSeleccionada;
	}

	public void setBicicletaSeleccionada(BicicletaDTO bicicletaSeleccionada) {
		this.bicicletaSeleccionada = bicicletaSeleccionada;
	}
}