package com.sofactory.dtos;

import java.util.Date;

public class MensajeDTO {

	private Long id;
	private Long usrdesde;
	private Long usrpara;
	private String texto;
	private Boolean status;
	private Date fecha;
	
	public MensajeDTO(){}
	
	public MensajeDTO(Long id, Long usrdesde, Long usrpara, String texto, Boolean status, Date fecha) {
		super();
		this.id = id;
		this.usrdesde = usrdesde;
		this.usrpara = usrpara;
		this.texto = texto;
		this.status = status;
		this.fecha = fecha;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getUsrdesde() {
		return usrdesde;
	}
	
	public Long getUsrpara() {
		return usrpara;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public Boolean getStatus() {
		return status;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUsrdesde(Long usrdesde) {
		this.usrdesde = usrdesde;
	}
	
	public void setUsrpara(Long usrpara) {
		this.usrpara = usrpara;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
		
}
