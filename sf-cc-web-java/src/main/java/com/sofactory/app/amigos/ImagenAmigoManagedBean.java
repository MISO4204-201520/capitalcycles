package com.sofactory.app.amigos;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.sofactory.dtos.AmigoDTO;

@ManagedBean
@RequestScoped
public class ImagenAmigoManagedBean implements Serializable {

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
				List<AmigoDTO> amigos = (List<AmigoDTO>)contexto.getExternalContext().
						getSessionMap().get("amigos");
				if (amigos != null && !amigos.isEmpty()){
					for (AmigoDTO a:amigos){
						if (a.getId()==Long.parseLong(idImagen)){
							if (a.getFoto()!=null){
								ByteArrayInputStream bytes=new ByteArrayInputStream(a.getFoto());
								return new DefaultStreamedContent(bytes);	
							}else{
								return null;
							}	
						}
					}
				}
			}
		}
		return new DefaultStreamedContent();
	}
}
