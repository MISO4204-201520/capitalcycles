package com.sofactory.servicios;

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

import com.sofactory.dtos.AlquilerDTO;
import com.sofactory.dtos.BicicletaAlquilerDTO;
import com.sofactory.dtos.RespuestaAlquilerDTO;
import com.sofactory.dtos.SitioAlquilerDTO;
import com.sofactory.dtos.SitioDTO;
import com.sofactory.entidades.Alquiler;
import com.sofactory.entidades.BicicletaAlquiler;
import com.sofactory.entidades.EstacionEntrega;
import com.sofactory.entidades.Estado;
import com.sofactory.entidades.SitioAlquiler;
import com.sofactory.negocio.interfaces.AlquilerBeanLocal;
import com.sofactory.negocio.interfaces.SitioAlquilerBeanLocal;

@Path("alquiler")
public class AlquilerService {

	private static String servicioRegistrarServicio = "http://localhost:8080/sf-cc-fidelizacion/rest/fidelizacion/registrarServicio";
	
	@EJB
	private SitioAlquilerBeanLocal sitioAlquilerBeanLocal;
	
	@EJB
	private AlquilerBeanLocal alquilerBeanLocal;
	
	@POST
	@Path("crearSitiosPredefinidos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaAlquilerDTO crearSitiosPredefinidos(
			SitioDTO sitioDTO) {
		RespuestaAlquilerDTO respuestaAlquilerDTO = new RespuestaAlquilerDTO();
		try {
			List<SitioAlquiler> lista = sitioAlquilerBeanLocal.encontrarPorColumna(SitioAlquiler.class, "nombre", sitioDTO.getNombre());
			if (lista==null || lista.isEmpty()){
				List<BicicletaAlquiler> bicicletaAlquileres = sitioAlquilerBeanLocal.encontrarTodosBicicletaAlquiler();
				List<EstacionEntrega> estacionEntregas = sitioAlquilerBeanLocal.encontrarTodosEstacionEntrega();
				SitioAlquiler sa = new SitioAlquiler();
				sa.setBicicletaAlquileres(bicicletaAlquileres);
				sa.setEstacionEntregas(estacionEntregas);
				sa.setNombre(sitioDTO.getNombre());
				sa.setDireccion(sitioDTO.getDireccion());
				sa.setLat(sitioDTO.getLatitud().toString());
				sa.setLng(sitioDTO.getLongitud().toString());
				sitioAlquilerBeanLocal.insertarOActualizar(sa);	
			}
		} catch (Exception e) {
			respuestaAlquilerDTO = new RespuestaAlquilerDTO();
			respuestaAlquilerDTO.setCodigo(1);
			respuestaAlquilerDTO.setMensaje("Hubo algun error en encontrar los sitios");
			e.printStackTrace();
		}
		return respuestaAlquilerDTO;
	}
	
	@GET
	@Path("encontrarSitioAlquilerPorNombre/{nombre}")
	@Produces("application/json")
	public RespuestaAlquilerDTO encontrarSitioAlquilerPorNombre(@PathParam("nombre") String nombre) {
		RespuestaAlquilerDTO respuestaAlquilerDTO = new RespuestaAlquilerDTO();
		try {
			List<SitioAlquiler> lista = sitioAlquilerBeanLocal.encontrarPorColumna(SitioAlquiler.class, "nombre", "\""+nombre+"\"");
			if (lista!=null && !lista.isEmpty()){
				SitioAlquiler sa = lista.get(0);
				SitioAlquilerDTO sitioAlquilerDTO = new SitioAlquilerDTO();
				sitioAlquilerDTO.setCodigoSitioAlquiler(sa.getCodigo());
				sitioAlquilerDTO.setNombre(sa.getNombre());
				sitioAlquilerDTO.setDireccion(sa.getDireccion());
				sitioAlquilerDTO.setLatitud(new Double(sa.getLat()));
				sitioAlquilerDTO.setLongitud(new Double(sa.getLng()));
				List<BicicletaAlquilerDTO> listaBici = new ArrayList<BicicletaAlquilerDTO>();
				List<BicicletaAlquiler> bicicletaAlquileres = sitioAlquilerBeanLocal.encontrarBicicletaAlquilerPorSitioAlquiler(sitioAlquilerDTO.getCodigoSitioAlquiler());
				for (BicicletaAlquiler bar:bicicletaAlquileres){
					BicicletaAlquilerDTO ba = new BicicletaAlquilerDTO();
					ba.setCodigo(bar.getCodigo());
					ba.setMarca(bar.getMarca());
					ba.setModelo(bar.getModelo());
					ba.setTarifa(bar.getTarifa());
					ba.setFoto(bar.getFoto());
					ba.setCantidad(bar.getCantidad());
					ba.setDisponibles(ba.getCantidad()-sitioAlquilerBeanLocal.bicicletasAlquiladas(ba.getCodigo(),sa.getCodigo()).intValue());
					listaBici.add(ba);
				}
				sitioAlquilerDTO.setBicicletaAlquilerDTOs(listaBici);
				respuestaAlquilerDTO.setSitioAlquilerDTO(sitioAlquilerDTO);
				return respuestaAlquilerDTO;
			}
		} catch (Exception e) {
			respuestaAlquilerDTO = new RespuestaAlquilerDTO();
			respuestaAlquilerDTO.setCodigo(1);
			respuestaAlquilerDTO.setMensaje("Hubo algun error en encontrar los sitios");
			e.printStackTrace();
		}
		return respuestaAlquilerDTO;
	}
	
	@POST
	@Path("crear")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaAlquilerDTO crear(
			AlquilerDTO alquilerDTO) {
		RespuestaAlquilerDTO respuestaAlquilerDTO = new RespuestaAlquilerDTO();
		try {
			respuestaAlquilerDTO.setCodigo(0);
			respuestaAlquilerDTO.setMensaje("OK");
			Alquiler alquiler = new Alquiler();
			alquiler.setCodigoUsuario(alquilerDTO.getCodigoUsuario());
			BicicletaAlquiler bicicletaAlquiler = new BicicletaAlquiler();
			bicicletaAlquiler.setCodigo(alquilerDTO.getCodigoBicicletaAlquiler());
			alquiler.setBicicletaAlquiler(bicicletaAlquiler);
			alquiler.setFechaAlquiler(alquilerDTO.getFechaAlquiler());
			alquiler.setEstado(Estado.ALQUILADA);
			SitioAlquiler sitioAlquiler = new SitioAlquiler();
			sitioAlquiler.setCodigo(alquilerDTO.getCodigoSitioAlquiler());
			alquiler.setSitioAlquiler(sitioAlquiler);
			alquilerBeanLocal.insertarOActualizar(alquiler);
		} catch (Exception e) {
			respuestaAlquilerDTO = new RespuestaAlquilerDTO();
			respuestaAlquilerDTO.setCodigo(1);
			respuestaAlquilerDTO.setMensaje("Hubo algun error en encontrar los sitios");
			e.printStackTrace();
		}
		return respuestaAlquilerDTO;
	}
}