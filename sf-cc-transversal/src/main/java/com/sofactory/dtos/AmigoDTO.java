package com.sofactory.dtos;

public class AmigoDTO {

	private Long id;
	private Long codUsuario;
	private Long codAmigo;
	private String nombres;
	private String apellidos;
	private String correo;
	private byte[] foto;
	
	public AmigoDTO(){}
	
	public AmigoDTO(Long id, Long codUsuario, Long codAmigo ) {
		super();
		this.id=id;
		this.codUsuario = codUsuario;
		this.codAmigo = codAmigo;
	}

	public Long getId() {
		return id;
	}

	public Long getCodUsuario() {
		return codUsuario;
	}

	public Long getCodAmigo() {
		return codAmigo;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setCodUsuario(Long codUsuario) {
		this.codUsuario = codUsuario;
	}

	public void setCodAmigo(Long codAmigo) {
		this.codAmigo = codAmigo;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
}