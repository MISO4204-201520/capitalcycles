package com.sofactory.app.alquiler;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.sofactory.dtos.BicicletaAlquilerDTO;

@ManagedBean
@RequestScoped
public class ImagenesBicicletaAlquilerManagedBean implements Serializable {

	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Método que obtiene la imagen con stream
	 * 
	 * @return la imagen con stream
	 */
	public StreamedContent getImagenStream(){
		FacesContext contexto = FacesContext.getCurrentInstance();
		if (contexto.getCurrentPhaseId() != PhaseId.RENDER_RESPONSE) {
			String idImagen = contexto.getExternalContext().getRequestParameterMap().get("idImagen");
			if (idImagen!=null){
				List<BicicletaAlquilerDTO> bicicletasAlquileres = (List<BicicletaAlquilerDTO>)contexto.getExternalContext().
						getSessionMap().get("bicicletasAlquileres");
				if (bicicletasAlquileres != null && !bicicletasAlquileres.isEmpty()){
					for (BicicletaAlquilerDTO ba:bicicletasAlquileres){
						if (ba.getCodigo()==Long.parseLong(idImagen)){
							ByteArrayInputStream bytes=new ByteArrayInputStream(ba.getFoto());
							return new DefaultStreamedContent(bytes);	
						}
					}
				}
			}
		}
		return new DefaultStreamedContent();
	}
}
