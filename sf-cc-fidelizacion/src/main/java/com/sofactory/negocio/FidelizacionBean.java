package com.sofactory.negocio;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;

import com.sofactory.dtos.RedimirProductoDTO;
import com.sofactory.dtos.RegistrarPuntosDTO;
import com.sofactory.entidades.MovimientoPunto;
import com.sofactory.entidades.Producto;
import com.sofactory.entidades.Punto;
import com.sofactory.entidades.Servicio;
import com.sofactory.excepciones.RegistroYaExisteException;
import com.sofactory.negocio.interfaz.FidelizacionBeanLocal;

@Stateless
public class FidelizacionBean implements FidelizacionBeanLocal{

	@EJB
	private MovimientoPuntoJPA movimientoJpa;
	
	@EJB
	private ProductoJPA productoJpa;
	
	@EJB
	private PuntoJPA puntoJpa;

	@EJB
	private ServicioJPA servicioJpa;
	
	@Override
	public void registrarServicio(RegistrarPuntosDTO dto) 
			throws ConstraintViolationException, RegistroYaExisteException {
		List<Servicio> servicios = servicioJpa.encontrarPorColumna
				(Servicio.class, "nombre", dto.getServicio());
		
		Servicio servicio = null;
		if (!servicios.isEmpty()){
			servicio = servicios.get(0);
		}
		
		List<Punto> puntos = puntoJpa.encontrarPorColumna
				(Punto.class, "codigoUsuario", dto.getCodigoUsuario());
		
		Punto punto = null;
		if (!puntos.isEmpty()){
			punto = puntos.get(0);
		}
		if (null==punto){
			punto = new Punto();
			punto.setCodigoUsuario(dto.getCodigoUsuario());
			punto.setPuntos(0);
			puntoJpa.insertar(punto);
		}
		
		punto.setPuntos(punto.getPuntos()+servicio.getPuntos());
		puntoJpa.insertarOActualizar(punto);
		
		MovimientoPunto movimiento = new MovimientoPunto();
		movimiento.setPunto(punto);
		movimiento.setRegistro(true);
		movimiento.setServicio(servicio);
		
		movimientoJpa.insertar(movimiento);
	}

	@Override
	public void redimirProducto(RedimirProductoDTO dto) 
			throws ConstraintViolationException, RegistroYaExisteException {
		Producto producto = productoJpa.getProducto(dto.getIdProducto());
		
		List<Punto> puntos = puntoJpa.encontrarPorColumna
				(Punto.class, "codigoUsuario", dto.getCodigoUsuario());
		
		Punto punto = null;
		if (!puntos.isEmpty()){
			punto = puntos.get(0);
		}
		
		if (null==punto){
			throw new RuntimeException("El usuario no cuenta con puntos");
		}
		
		if (punto.getPuntos()<producto.getPuntos()){
			throw new RuntimeException("El usuario no cuenta con suficientes puntos");
		}
		
		punto.setPuntos(punto.getPuntos()-producto.getPuntos());
		puntoJpa.insertarOActualizar(punto);
		
		MovimientoPunto movimiento = new MovimientoPunto();
		movimiento.setPunto(punto);
		movimiento.setRegistro(false);
		movimiento.setProducto(producto);
		
		movimientoJpa.insertar(movimiento);
	}

	@Override
	public List<Producto> obtenerCatalogoProdutos() {
		return productoJpa.encontrarTodos(Producto.class, "nombre", "asc");
	}
	
	@Override
	public Integer obtenerPuntosUsuario(String codigoUsuario){
		List<Punto> puntos = puntoJpa.encontrarPorColumna
				(Punto.class, "codigoUsuario", codigoUsuario);
		
		if (null==puntos || puntos.isEmpty()){
			return 0;
		}
		
		return puntos.get(0).getPuntos();
	}
}
