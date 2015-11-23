package com.sofactory.negocio;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.sofactory.dtos.ParteDTO;
import com.sofactory.entidades.Bicicleta;
import com.sofactory.entidades.ListadoPartes;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.BicicletaBeanLocal;

@Stateless
@Local({ BicicletaBeanLocal.class })
public class BicicletaBean extends GenericoBean<Bicicleta> implements BicicletaBeanLocal{
	@PersistenceContext(unitName="ConfiguradorBiciPU")
	private EntityManager em;
	
	@EJB
	private BiciDirectorBean biciDirectorBean;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	@Override
	public List<ListadoPartes> getListadoPartes(String[] listadoPartes){
		String sql = "SELECT lp FROM ListadoPartes lp WHERE lp.nombreFeauture IN :listadoPartes ";
		TypedQuery<ListadoPartes> lista = em.createQuery(sql, ListadoPartes.class);
		lista.setParameter("listadoPartes", Arrays.asList(listadoPartes));
		return lista.getResultList();
	}

	@Override
	public void construirBicicleta(List<ParteDTO> partes) {
		biciDirectorBean.construirBicicleta(partes);
		Bicicleta bicicleta = biciDirectorBean.getBicicleta();
		bicicleta.setCodigoUsuario(partes.get(0).getCodigoUsuario());
		bicicleta.setNombre(partes.get(0).getNombreConfiguracion());
		em.merge(bicicleta);
	}

	@Override
	public List<Bicicleta> encontrarConfiguracionesPorUsuario(Long codigoUsuario) {
		List<Bicicleta> lista = null;
		try {
			TypedQuery<Bicicleta> typedQuery = em.createQuery("SELECT b FROM Bicicleta b WHERE b.codigoUsuario = :codigoUsuario ", Bicicleta.class);
			typedQuery.setParameter("codigoUsuario", codigoUsuario);
			lista = typedQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return lista;
	}
}