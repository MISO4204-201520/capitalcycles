package com.sofactory.servicio;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.CatalogoDTO;
import com.sofactory.dtos.RedimirProductoDTO;
import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.negocio.interfaz.FidelizacionBeanLocal;

@Path("fidelizacion")
public class FidelizacionServices {

	@EJB
	private FidelizacionBeanLocal fidelizacionBean;
	
	@POST
	@Path("registrarServicio")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaDTO registrarServicio(RegistrarPuntosDTO dto){
		RespuestaDTO response = new RespuestaDTO();
		try {
			fidelizacionBean.registrarServicio();
			response.setMensaje("OK");
		}catch (Exception e) {
			e.printStackTrace();
			response.setMensaje("ERROR-"+e.getMessage());
		}
		
		return response;
	}
	
	@POST
	@Path("redimirProducto")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaDTO redimirProducto(RedimirProductoDTO dto){
		RespuestaDTO response = new RespuestaDTO();
		try {
			fidelizacionBean.redimirProducto();
			response.setMensaje("OK");
		}catch (Exception e) {
			e.printStackTrace();
			response.setMensaje("ERROR-"+e.getMessage());
		}
		
		return response;
	}
	
	@GET
	@Path("catalogoProdutos")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CatalogoDTO catalogoProdutos() {
		CatalogoDTO response = new CatalogoDTO();
		try {
			fidelizacionBean.obtenerCatalogoProdutos();
			response.setMensaje("OK");
		}catch (Exception e) {
			e.printStackTrace();
			response.setMensaje("ERROR-"+e.getMessage());
		}
		
		return response;
	}
}
