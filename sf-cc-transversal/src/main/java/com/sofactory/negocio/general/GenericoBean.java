package com.sofactory.negocio.general;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

import com.sofactory.excepciones.CorreoInvalidoException;
import com.sofactory.excepciones.ReferenciaException;
import com.sofactory.excepciones.RegistroYaExisteException;

public abstract class GenericoBean<T> {

	@PersistenceContext(unitName="CapitalCyclesPU")
	protected EntityManager em;

	/**
	 * Método que sirve para obtener todos los registros de una tabla específica
	 * 
	 * @param clase,
	 *            es la tabla a buscar en la base de datos
	 * @return lista que representa los registros de la base de datos de una
	 *         tabla
	 */
	public List<T> encontrarTodos(Class<T> clase) {
		List<T> lista = null;
		try {
			TypedQuery<T> typedQuery = em.createQuery("SELECT t FROM " + clase.getSimpleName() + " t", clase);
			lista = typedQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return lista;
	}

	/**
	 * Método que sirve para encontrar un registro por id
	 * 
	 * @param clase,
	 *            es la tabla a buscar en la base de datos
	 * @param id,
	 *            es el id de la tabla
	 * @return el registro por id
	 */
	public T encontrarPorId(Class<T> clase, Object id) {
		T t = null;
		try {
			t = em.find(clase, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return t;
	}

	/**
	 * Método que sirve para agregar registros a la base de datos
	 * 
	 * @param t,
	 *            es el objeto a guardar
	 * @throws RegistroYaExisteException,
	 *             si el usuario ya existe
	 * @throws CorreoInvalidoException, si el correo es invalido
	 */
	public void insertar(T t) throws RegistroYaExisteException,CorreoInvalidoException {
		try {
			 em.persist(t);
		} catch (Exception e) {
			if (e instanceof ConstraintViolationException){
				throw new CorreoInvalidoException("Correo invalido", e);
			}
			else if (e.getCause() != null && e.getCause().getCause() != null) {
				throw new RegistroYaExisteException("El registro ya existe en la base de datos", e);
			}
		} finally {

		}
	}

	/**
	 * Método que sirve para insertar o actualizar un registro a la base de
	 * datos
	 * 
	 * @param t,
	 *            es el objeto a guardar o actualizar
	 * @return t, es el objeto actualizado
	 */
	public T insertarOActualizar(T t) {
		T newT = null;
		try {
			newT = em.merge(t);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return newT;
	}

	/**
	 * Método que sirve para remover un registro de la base de datos
	 * 
	 * @param t,
	 *            es el objeto a eliminar
	 * @param id,
	 *            es el identificador del registro
	 * @throws ReferenciaException,
	 *             si existe referencias a otras tablas
	 */
	public void remover(T t, Object id) throws ReferenciaException {
		try {
			t = (T) em.find(t.getClass(), id);
			em.remove(t);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getCause() != null && e.getCause().getCause() != null
					&& e.getCause().getCause() instanceof ConstraintViolationException) {
				throw new ReferenciaException("El registro tiene referencias a otras tablas", e);
			}
		} finally {

		}
	}
	
	/**
	 * Método que sirve para encontrar un registro por columna
	 * 
	 * @param clase,
	 *            es la tabla a buscar en la base de datos
	 * @param columna,
	 *            es la columna de la tabla a buscar
	 * @param valor, es el valor de la columna a buscar el registro
	 * @return el registro por columna
	 */
	public List<T> encontrarPorColumna(Class<T> clase, String columna, String valor){
		List<T> lista = null;
		try {
			TypedQuery<T> typedQuery = em.createQuery("SELECT t FROM " + clase.getSimpleName() + " t WHERE t."+columna+"= :valor ", clase);
			typedQuery.setParameter("valor", valor);
			lista = typedQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return lista;
	}
}