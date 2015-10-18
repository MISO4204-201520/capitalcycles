package com.sofactory.app.fidelizacion;

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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.sofactory.app.dto.ProductoSelectDTO;
import com.sofactory.app.seguridad.UsuarioManagedBean;
import com.sofactory.dtos.CatalogoDTO;
import com.sofactory.dtos.ProductoDTO;
import com.sofactory.dtos.PuntosUsuarioDTO;
import com.sofactory.dtos.RedimirProductoDTO;
import com.sofactory.dtos.RespuestaDTO;

@ManagedBean
@ViewScoped
public class FidelizacionManagedBean implements Serializable{

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private final static String R_CATALOGO = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/catalogoProductos";

	private final static String R_PUNTOS_USUARIO = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/obtenerPuntoUsuario";

	private final static String R_REDIMIR = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/redimirProducto";

	private List<ProductoSelectDTO> productos;
	
	private Integer puntosUsuario;
	
	/** Atributo usuario managed bean. */
	@ManagedProperty("#{usuarioManagedBean}")
	private UsuarioManagedBean usuarioManagedBean;
	
	@PostConstruct
	public void inicializar(){
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(R_CATALOGO+"/"+usuarioManagedBean.getUsuarioDTO().getCodigo());
		CatalogoDTO catalogo = messages.request("application/json")
				.get(CatalogoDTO.class);
		
		messages = client.target(R_PUNTOS_USUARIO+"/"+usuarioManagedBean.getUsuarioDTO().getCodigo());
		PuntosUsuarioDTO puntosUsuario = messages.request("application/json")
				.get(PuntosUsuarioDTO.class);
		
		this.puntosUsuario = puntosUsuario.getPuntos();
		
		productos = new ArrayList<ProductoSelectDTO>();
		for (ProductoDTO dto : catalogo.getProductos()) {
			productos.add(new ProductoSelectDTO
					(dto.getId(), dto.getNombre(), dto.getPuntos(), false));
		}
	}
	
	public void redimir(Long idProducto){
		Client client = ClientBuilder.newClient();
		WebTarget messages = client.target(R_REDIMIR);
		RedimirProductoDTO dto = new RedimirProductoDTO();
		dto.setCodigoUsuario(usuarioManagedBean.getUsuarioDTO().getCodigo().toString());
		dto.setIdProducto(idProducto);
		RespuestaDTO respuesta = messages.request("application/json")
				.post(Entity.entity(dto, MediaType.APPLICATION_JSON),RespuestaDTO.class);
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(
						FacesMessage.SEVERITY_INFO, 
						null, 
						respuesta.getMensaje()));
	}
	
	public Integer getPuntosUsuario() {
		return puntosUsuario;
	}

	public void setPuntosUsuario(Integer puntosUsuario) {
		this.puntosUsuario = puntosUsuario;
	}

	public UsuarioManagedBean getUsuarioManagedBean() {
		return usuarioManagedBean;
	}

	public void setUsuarioManagedBean(UsuarioManagedBean usuarioManagedBean) {
		this.usuarioManagedBean = usuarioManagedBean;
	}

	public List<ProductoSelectDTO> getProductos() {
		return productos;
	}

	public void setProductos(List<ProductoSelectDTO> productos) {
		this.productos = productos;
	}
}
