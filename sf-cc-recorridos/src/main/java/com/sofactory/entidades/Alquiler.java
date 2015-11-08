package com.sofactory.entidades;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "AL_ALQUILER")
public class Alquiler implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODIGO")
	private Long codigo;

	@ManyToOne
	@JoinColumn(name="CODIGO_BICICLETA_ALQUILER")
	private BicicletaAlquiler bicicletaAlquiler;
	
	@ManyToOne
	@JoinColumn(name="CODIGO_SITIO_ALQUILER")
	private SitioAlquiler sitioAlquiler;
	
	@Column(name="CODIGO_USUARIO")
	private Long codigoUsuario;
	
	@Column(name="FECHA_ALQUILER")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fechaAlquiler;
	
	@Column(name="FECHA_ENTREGA")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fechaEntrega;
	
	@Column(name="ESTADO")
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	@Column(name="LIMITE_TIEMPO_ENTREGA")
	private Long limiteTiempoEntrega;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public BicicletaAlquiler getBicicletaAlquiler() {
		return bicicletaAlquiler;
	}

	public void setBicicletaAlquiler(BicicletaAlquiler bicicletaAlquiler) {
		this.bicicletaAlquiler = bicicletaAlquiler;
	}

	public Long getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public Calendar getFechaAlquiler() {
		return fechaAlquiler;
	}

	public void setFechaAlquiler(Calendar fechaAlquiler) {
		this.fechaAlquiler = fechaAlquiler;
	}

	public Calendar getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Calendar fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Long getLimiteTiempoEntrega() {
		return limiteTiempoEntrega;
	}

	public void setLimiteTiempoEntrega(Long limiteTiempoEntrega) {
		this.limiteTiempoEntrega = limiteTiempoEntrega;
	}

	public SitioAlquiler getSitioAlquiler() {
		return sitioAlquiler;
	}

	public void setSitioAlquiler(SitioAlquiler sitioAlquiler) {
		this.sitioAlquiler = sitioAlquiler;
	}
}
