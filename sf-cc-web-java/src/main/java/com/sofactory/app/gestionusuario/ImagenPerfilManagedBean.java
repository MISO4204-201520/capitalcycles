package com.sofactory.app.gestionusuario;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class ImagenPerfilManagedBean implements Serializable{
	
	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private byte[] imgActualizar;
	
	private StreamedContent imagenActualizar;

	private UploadedFile uploadFileActualizar;
	
	public byte[] getImgActualizar() {
		return imgActualizar;
	}

	public void setImgActualizar(byte[] imgActualizar) {
		this.imgActualizar = imgActualizar;
	}

	public StreamedContent getImagenActualizar() {
		imagenActualizar=null;
		
		if(imgActualizar!=null){
			imagenActualizar = new DefaultStreamedContent(new ByteArrayInputStream(imgActualizar));
		}
		return imagenActualizar;
	}

	public void setImagenActualizar(StreamedContent imagenActualizar) {
		this.imagenActualizar = imagenActualizar;
	}

	public UploadedFile getUploadFileActualizar() {
		return uploadFileActualizar;
	}

	public void setUploadFileActualizar(UploadedFile uploadFileActualizar) {
		this.uploadFileActualizar = uploadFileActualizar;
	}	
}