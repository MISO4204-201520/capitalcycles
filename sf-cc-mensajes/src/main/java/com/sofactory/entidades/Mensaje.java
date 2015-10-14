package com.sofactory.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ME_MENSAJE")
public class Mensaje implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "USRDESDE")
	private Long usrdesde;

	@Column(name = "USRPARA")
	private Long usrpara;
	
	@Column(name = "TEXTO")
	private String texto;
	
	@Column(name = "STATUS")
	private Boolean status;

	@Column(name = "FECHA")
	private Date fecha;

	public static long getSerialversionuid() {
		return serialVersionUID;
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
