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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.CatalogoDTO;
import com.sofactory.dtos.HistorialPuntosDTO;
import com.sofactory.dtos.MovimientoPuntoDTO;
import com.sofactory.dtos.ProductoDTO;
import com.sofactory.dtos.PuntosUsuarioDTO;
import com.sofactory.dtos.RedimirProductoDTO;
import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.dtos.RespuestaSeguridadDTO;
import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.MovimientoPunto;
import com.sofactory.entidades.Producto;
import com.sofactory.negocio.interfaz.FidelizacionBeanLocal;

@Path("fidelizacion")
public class FidelizacionServices {

	private static String servicioObtenerUsuarioSesion = "http://localhost:8080/sf-cc-gestion-usuario/rest/seguridadService/obtenerUsuarioSesion";
	
	@EJB
	private FidelizacionBeanLocal fidelizacionBean;
	
	@POST
	@Path("registrarServicio")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaDTO registrarServicio(RegistrarPuntosDTO dto){
		
		RespuestaDTO response = new RespuestaDTO();
		response.setCodigoUsuario(dto.getCodigoUsuario());
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(new Long(dto.getCodigoUsuario()));
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		if (resu.getCodigo()==0){
			
			try {
				fidelizacionBean.registrarServicio(dto);
				response.setMensaje("OK");
			}catch (Exception e) {
				e.printStackTrace();
				response.setMensaje("ERROR-"+e.getMessage());
			}
		}else{
			
			response.setCodigo(10);
			response.setMensaje(resu.getMensaje());
			
		}
		
		return response;
	}
	
	@POST
	@Path("redimirProducto")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaDTO redimirProducto(RedimirProductoDTO dto){
		
		RespuestaDTO response = new RespuestaDTO();
		response.setCodigoUsuario(dto.getCodigoUsuario());
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(new Long(dto.getCodigoUsuario()));
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		if (resu.getCodigo()==0){
				
			try {
				fidelizacionBean.redimirProducto(dto);
				response.setMensaje("OK");
			}catch (Exception e) {
				e.printStackTrace();
				response.setMensaje("ERROR-"+e.getMessage());
			}
		}else{
			
			response.setCodigo(10);
			response.setMensaje(resu.getMensaje());
			
		}
		
		return response;
	}
	
	@GET
	@Path("catalogoProductos/{codigoUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CatalogoDTO catalogoProductos(@PathParam("codigoUsuario")String codigoUsuario) {
		
		CatalogoDTO response = new CatalogoDTO();
		response.setCodigoUsuario(codigoUsuario);

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(new Long(codigoUsuario));
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		if (resu.getCodigo()==0){
			
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
		}else{
			
			response.setCodigo(10);
			response.setMensaje(resu.getMensaje());
			
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
		response.setCodigoUsuario(codigoUsuario);
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(new Long(codigoUsuario));
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		if (resu.getCodigo()==0){
		
			try {
				Integer puntos = fidelizacionBean.obtenerPuntosUsuario(codigoUsuario);
				response.setPuntos(puntos);
				response.setCodigoUsuario(codigoUsuario);
				response.setMensaje("OK");
			}catch (Exception e) {
				e.printStackTrace();
				response.setMensaje("ERROR-"+e.getMessage());
			}
		}else{
			
			response.setCodigo(10);
			response.setMensaje(resu.getMensaje());
			
		}
		
		return response;
	}
	
	@GET
	@Path("obtenerHistorial/{codigoUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public HistorialPuntosDTO obtenerHistorial
		(@PathParam("codigoUsuario")String codigoUsuario) {
	
		HistorialPuntosDTO response = new HistorialPuntosDTO();
		response.setCodigoUsuario(codigoUsuario);
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setCodigo(new Long(codigoUsuario));
	
		Client client = ClientBuilder.newClient();
		WebTarget targetMensaje = client.target(servicioObtenerUsuarioSesion);
		RespuestaSeguridadDTO resu = targetMensaje.request("application/json").
				post(Entity.entity(usuarioDTO, MediaType.APPLICATION_JSON),RespuestaSeguridadDTO.class);
		
		if (resu.getCodigo()==0){
		
			try {
				List<MovimientoPunto> movimientos =
						fidelizacionBean.obtenerHistorial(codigoUsuario);
				response.setMovimientos(new ArrayList<MovimientoPuntoDTO>());
				MovimientoPuntoDTO mvDto;
				
				for (MovimientoPunto mp : movimientos) {
					mvDto = new MovimientoPuntoDTO();
					mvDto.setEsRegistro(mp.getRegistro());
					mvDto.setFecha(mp.getFecha());
					if (mp.getRegistro()){
						mvDto.setMovimiento(mp.getServicio().getNombre());
						mvDto.setPuntos(mp.getServicio().getPuntos());
					}else{
						mvDto.setMovimiento(mp.getProducto().getNombre());
						mvDto.setPuntos(mp.getProducto().getPuntos());
					}
					
					response.getMovimientos().add(mvDto);
				}
				
				response.setCodigoUsuario(codigoUsuario);
				response.setMensaje("OK");
			}catch (Exception e) {
				e.printStackTrace();
				response.setMensaje("ERROR-"+e.getMessage());
			}
		}else{
			
			response.setCodigo(10);
			response.setMensaje(resu.getMensaje());
			
		}
		
		return response;
	}
}
