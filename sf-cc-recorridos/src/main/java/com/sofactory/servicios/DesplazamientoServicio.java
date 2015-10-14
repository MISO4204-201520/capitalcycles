package com.sofactory.servicios;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sofactory.dtos.RegistrarPosicionDTO;
import com.sofactory.dtos.RespuestaDTO;
import com.sofactory.dtos.RutaDTO;
import com.sofactory.entidades.PosicionTiempo;
import com.sofactory.negocio.interfaces.DesplazamientoBeanLocal;

@Path("desplazamiento")
public class DesplazamientoServicio {

	@EJB
	private DesplazamientoBeanLocal desplazamientoBean;

	@POST
	@Path("iniciarDesplazamiento")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaDTO iniciarDesplazamiento(RutaDTO dto){
		RespuestaDTO response = new RespuestaDTO();
		try {
			desplazamientoBean.iniciarDesplazamiento(dto.getId(), dto.getCodigoUsuario());
			response.setMensaje("OK");
		}catch (Exception e) {
			e.printStackTrace();
			response.setMensaje("ERROR-"+e.getMessage());
		}
		
		return response;
	}

	@POST
	@Path("registrarPosicion")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RespuestaDTO registrarPosicion(RegistrarPosicionDTO dto){
		RespuestaDTO response = new RespuestaDTO();
		try {
			PosicionTiempo pt = new PosicionTiempo();
			pt.setLatitud(dto.getLatitud());
			pt.setLongitud(dto.getLongitud());
			pt.setTiempo(dto.getTiempo());
			desplazamientoBean.registrarPosicion(pt, dto.getIdRuta());
			response.setMensaje("OK");
		}catch (Exception e) {
			e.printStackTrace();
			response.setMensaje("ERROR-"+e.getMessage());
		}
		
		return response;
	}
}
