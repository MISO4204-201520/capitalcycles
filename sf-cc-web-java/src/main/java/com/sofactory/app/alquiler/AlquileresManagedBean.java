package com.sofactory.app.alquiler;


import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.primefaces.model.map.MapModel;

import com.google.gson.Gson;
import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.AlquilerDTO;
import com.sofactory.dtos.BicicletaAlquilerDTO;
import com.sofactory.dtos.RespuestaAlquilerDTO;
import com.sofactory.dtos.RespuestaSitioDTO;
import com.sofactory.dtos.SitioAlquilerDTO;
import com.sofactory.dtos.SitioDTO;

@ManagedBean
@ViewScoped
public class AlquileresManagedBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;
	
	private String lat;
	
	private String lng;
	
	private MapModel mapModel;

	private String radio;
	
	private String sitio;
	
	private String alquileresJson;
	
	private String getEncontrarSitios = "http://localhost:8080/sf-cc-recorridos/rest/sitio/encontrarSitios/";
	
	private String crearRelacionesBicicletaEstacion = "http://localhost:8080/sf-cc-recorridos/rest/alquiler/crearSitiosPredefinidos/";
	
	private String getEncontrarSitioAlquilerPorNombre = "http://localhost:8080/sf-cc-recorridos/rest/alquiler/encontrarSitioAlquilerPorNombre/";
	
	private String postCrearAlquiler = "http://localhost:8080/sf-cc-recorridos/rest/alquiler/crear/";
	
	private String nombreSitioSeleccionado;
	
	private SitioAlquilerDTO sitioSeleccionado;
	
	private List<BicicletaAlquilerDTO> bicicletaAlquilerDTOs;
	
	private boolean visibleBicicletas;
	
	private BicicletaAlquilerDTO bicicletaSeleccionada;
	
	private boolean visibleEE;
	
	@PostConstruct
	private void iniciar(){
		radio = "2000";
		sitio = "bicicleta";
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("bicicletasAlquileres");
	}

	public String buscarSitios(){
		if (usuarioManagedBean!=null && usuarioManagedBean.getUsuarioDTO()!=null){
			String servicio=getEncontrarSitios+lat+"/"+lng+"/"+radio+"/"+sitio.replaceAll(" ", "+")+"/"+usuarioManagedBean.getUsuarioDTO().getCodigo();
			Client client = ClientBuilder.newClient();
			WebTarget messages = client.target(servicio);
			RespuestaSitioDTO respuesta = messages.request("application/json").get(RespuestaSitioDTO.class);
			if (respuesta!=null){
				if (respuesta.getCodigo()==0){
					Gson gson = new Gson();
					alquileresJson = gson.toJson(respuesta.getSitios());
					if (respuesta.getSitios()!=null && !respuesta.getSitios().isEmpty()){
						client = ClientBuilder.newClient();
						messages = client.target(crearRelacionesBicicletaEstacion);
						for (SitioDTO sitioDTO:respuesta.getSitios()){
							messages.request("application/json").post(Entity.entity(sitioDTO, MediaType.APPLICATION_JSON),RespuestaAlquilerDTO.class);	
						}
					}
				}
			}
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							"No se encuentra en sesión"));	
		}
		return null;
	}
	
	public String mostrarBicicletasSitio(){
		visibleBicicletas = false;
		String servicio=getEncontrarSitioAlquilerPorNombre+nombreSitioSeleccionado;
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(servicio);
		RespuestaAlquilerDTO respuesta = messages.request("application/json").get(RespuestaAlquilerDTO.class);
		if (respuesta!=null && respuesta.getCodigo()==0 && respuesta.getSitioAlquilerDTO()!=null  && respuesta.getSitioAlquilerDTO().getBicicletaAlquilerDTOs()!=null && !respuesta.getSitioAlquilerDTO().getBicicletaAlquilerDTOs().isEmpty()){
			sitioSeleccionado = respuesta.getSitioAlquilerDTO();
			this.bicicletaAlquilerDTOs = respuesta.getSitioAlquilerDTO().getBicicletaAlquilerDTOs();
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bicicletasAlquileres", bicicletaAlquilerDTOs);
			visibleBicicletas = true;
		}
		return null;
	}
	
	public String alquilar(){
		if (bicicletaSeleccionada!=null){
			if (bicicletaSeleccionada.getDisponibles()>0){
				AlquilerDTO alquilerDTO = new AlquilerDTO();
				alquilerDTO.setCodigoBicicletaAlquiler(bicicletaSeleccionada.getCodigo());
				alquilerDTO.setCodigoUsuario(usuarioManagedBean.getUsuarioDTO().getCodigo());
				alquilerDTO.setFechaAlquiler(Calendar.getInstance());
				alquilerDTO.setCodigoSitioAlquiler(sitioSeleccionado.getCodigoSitioAlquiler());
				Client client = ClientBuilder.newClient();
				WebTarget messages = client.target(postCrearAlquiler);
				RespuestaAlquilerDTO respuesta = messages.request("application/json").post(Entity.entity(alquilerDTO, MediaType.APPLICATION_JSON),RespuestaAlquilerDTO.class);
				if (respuesta!=null && respuesta.getCodigo()==0){
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(
									FacesMessage.SEVERITY_INFO, 
									null, 
									"El alquiler se realizó correctamente"));
					FacesContext context = FacesContext.getCurrentInstance();
					context.getExternalContext().getFlash().setKeepMessages(true);
					return "CORRECTO";
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, 
								null, 
								"Bicicleta no disponible para alquilar"));				
			}
		}else{
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR, 
							null, 
							"Debe seleccionar una bicicleta para alquilar"));
		}
		return null;
	}
	
	public void limpiar(){
		this.lat = "";
		this.lng = "";
		this.sitioSeleccionado = null;
		this.bicicletaAlquilerDTOs = null;
		this.visibleBicicletas = false;
	}
	
	public void verEstacionesEntrega(){
		this.visibleEE = true;
	}
	
	public void cerrarEE(){
		this.visibleEE = false;
	}
	
	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}
	
	public String getAlquileresJson() {
		return alquileresJson;
	}

	public void setAlquileresJson(String alquileresJson) {
		this.alquileresJson = alquileresJson;
	}

	public String getNombreSitioSeleccionado() {
		return nombreSitioSeleccionado;
	}

	public void setNombreSitioSeleccionado(String nombreSitioSeleccionado) {
		this.nombreSitioSeleccionado = nombreSitioSeleccionado;
	}

	public List<BicicletaAlquilerDTO> getBicicletaAlquilerDTOs() {
		return bicicletaAlquilerDTOs;
	}

	public void setBicicletaAlquilerDTOs(List<BicicletaAlquilerDTO> bicicletaAlquilerDTOs) {
		this.bicicletaAlquilerDTOs = bicicletaAlquilerDTOs;
	}

	public boolean isVisibleBicicletas() {
		return visibleBicicletas;
	}

	public void setVisibleBicicletas(boolean visibleBicicletas) {
		this.visibleBicicletas = visibleBicicletas;
	}

	public BicicletaAlquilerDTO getBicicletaSeleccionada() {
		return bicicletaSeleccionada;
	}

	public void setBicicletaSeleccionada(BicicletaAlquilerDTO bicicletaSeleccionada) {
		this.bicicletaSeleccionada = bicicletaSeleccionada;
	}

	public boolean isVisibleEE() {
		return visibleEE;
	}

	public void setVisibleEE(boolean visibleEE) {
		this.visibleEE = visibleEE;
	}

	public SitioAlquilerDTO getSitioSeleccionado() {
		return sitioSeleccionado;
	}

	public void setSitioSeleccionado(SitioAlquilerDTO sitioSeleccionado) {
		this.sitioSeleccionado = sitioSeleccionado;
	}
}