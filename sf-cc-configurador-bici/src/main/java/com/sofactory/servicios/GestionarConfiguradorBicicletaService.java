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

import com.sofactory.dtos.BicicletaDTO;
import com.sofactory.dtos.EspecificacionDTO;
import com.sofactory.dtos.ListadoPartesDTO;
import com.sofactory.dtos.ParteDTO;
import com.sofactory.dtos.RespuestaBicicletaDTO;
import com.sofactory.entidades.Bicicleta;
import com.sofactory.entidades.Especificacion;
import com.sofactory.entidades.ListadoPartes;
import com.sofactory.entidades.Parte;
import com.sofactory.negocio.interfaces.BicicletaBeanLocal;

@Path("gestionarCBService")
public class GestionarConfiguradorBicicletaService {
	

	@EJB
	private BicicletaBeanLocal bicicletaBeanLocal;
	
	@GET
	@Path("encontrarListadoPartes/{nombrePartes}")
	@Produces("application/json")
	public RespuestaBicicletaDTO encontrarListadoPartes(@PathParam(value="nombrePartes") String nombrePartes) {
		RespuestaBicicletaDTO respuestaBicicletaDTO = new RespuestaBicicletaDTO(0, "OK");
		try{
			String[] splitFeature = nombrePartes.split(",");
			
			List<ListadoPartes> listadoPartes = bicicletaBeanLocal.getListadoPartes(splitFeature);
			for (ListadoPartes lp : listadoPartes){
				ListadoPartesDTO lpDTO =  new ListadoPartesDTO();
				lpDTO.setCodigo(lp.getCodigo());
				lpDTO.setNombreFeauture(lp.getNombreFeauture());
				lpDTO.setNombrePadreFeature(lp.getNombrePadreFeature());
				lpDTO.setNombreClaseParte(lp.getNombreClaseParte());
				for (Especificacion e:lp.getEspecificaciones()){
					EspecificacionDTO eDTO = new EspecificacionDTO();
					eDTO.setCodigo(e.getCodigo());
					eDTO.setColor(e.getColor());
					eDTO.setMarca(e.getMarca());
					eDTO.setPrecio(e.getPrecio());
					lpDTO.getEspecificaciones().add(eDTO);
				}
				respuestaBicicletaDTO.getListadoPartesDTOs().add(lpDTO);
			}
		}catch(Exception e){
			respuestaBicicletaDTO.setCodigo(1);
			respuestaBicicletaDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		return respuestaBicicletaDTO;
	}

	@POST
	@Path("crearConfiguracion")
	@Consumes("application/json")
	@Produces("application/json")
	public RespuestaBicicletaDTO crearConfiguracion(List<ParteDTO> partes) {
		RespuestaBicicletaDTO respuestaBicicletaDTO = new RespuestaBicicletaDTO(0, "OK");
		try{
			if (partes!=null && !partes.isEmpty()){
				bicicletaBeanLocal.construirBicicleta(partes);	
			}else{
				respuestaBicicletaDTO.setCodigo(2);
				respuestaBicicletaDTO.setMensaje("No hay partes para guardar la configuracion");
			}
		}catch(Exception e){
			respuestaBicicletaDTO.setCodigo(1);
			respuestaBicicletaDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		return respuestaBicicletaDTO;
	}
	
	@GET
	@Path("encontrarConfiguracionesPorUsuario/{codigoUsuario}")
	@Produces("application/json")
	public RespuestaBicicletaDTO encontrarConfiguracionesPorUsuario(@PathParam(value="codigoUsuario") String codigoUsuario) {
		RespuestaBicicletaDTO respuestaBicicletaDTO = new RespuestaBicicletaDTO(0, "OK");
		try{
			
			List<Bicicleta> listadoBicicletas = bicicletaBeanLocal.encontrarConfiguracionesPorUsuario(new Long(codigoUsuario));
			respuestaBicicletaDTO.setBicicletaDTOs(new ArrayList<BicicletaDTO>());
			for (Bicicleta b : listadoBicicletas){
				BicicletaDTO bDTO = new BicicletaDTO();
				bDTO.setCodigoUsuario(b.getCodigoUsuario());
				bDTO.setId(b.getId());
				bDTO.setNombre(b.getNombre());
				bDTO.setPartes(new ArrayList<ParteDTO>());
				for (Parte p:b.getPartes()){
					ParteDTO pDTO = new ParteDTO();
					pDTO.setCodigo(p.getCodigo());
					pDTO.setNombre(p.getNombre());
					pDTO.setColor(p.getColor());
					pDTO.setMarca(p.getMarca());
					pDTO.setPrecio(p.getPrecio());
					bDTO.getPartes().add(pDTO);
				}
				respuestaBicicletaDTO.getBicicletaDTOs().add(bDTO);
			}
		}catch(Exception e){
			respuestaBicicletaDTO.setCodigo(1);
			respuestaBicicletaDTO.setMensaje("Hubo un error en el sistema");
			e.printStackTrace();
		}
		return respuestaBicicletaDTO;
	}
}