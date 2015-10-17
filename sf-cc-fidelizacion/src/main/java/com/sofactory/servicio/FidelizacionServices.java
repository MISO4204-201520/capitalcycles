package com.sofactory.servicio;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.CatalogoDTO;
import com.sofactory.dtos.ProductoDTO;
import com.sofactory.dtos.PuntosUsuarioDTO;
import com.sofactory.dtos.RedimirProductoDTO;
import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.entidades.Producto;
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
			fidelizacionBean.registrarServicio(dto);
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
			fidelizacionBean.redimirProducto(dto);
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
			List<Producto> producto =
					fidelizacionBean.obtenerCatalogoProdutos();
			
			response.setProductos(new ArrayList<ProductoDTO>());
			ProductoDTO dto;
			for (Producto producto2 : producto) {
				dto = new ProductoDTO();
				dto.setId(producto2.getId());
				dto.setNombre(producto2.getNombre());
				dto.setPuntos(producto2.getPuntos());
				response.getProductos().add(dto);
			}
			
			response.setMensaje("OK");
		}catch (Exception e) {
			e.printStackTrace();
			response.setMensaje("ERROR-"+e.getMessage());
		}
		
		return response;
	}
	
	@GET
	@Path("obtenerPuntoUsuario/{codigoUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public PuntosUsuarioDTO obtenerPuntoUsuario
		(@PathParam("codigoUsuario")String codigoUsuario) {
		PuntosUsuarioDTO response = new PuntosUsuarioDTO();
		try {
			Integer puntos = fidelizacionBean.obtenerPuntosUsuario(codigoUsuario);
			response.setPuntos(puntos);
			response.setCodigoUsuario(codigoUsuario);
			response.setMensaje("OK");
		}catch (Exception e) {
			e.printStackTrace();
			response.setMensaje("ERROR-"+e.getMessage());
		}
		
		return response;
	}
}
