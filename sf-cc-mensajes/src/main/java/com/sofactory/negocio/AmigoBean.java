package com.sofactory.negocio;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sofactory.dtos.UsuarioDTO;
import com.sofactory.entidades.Amigo;
import com.sofactory.negocio.general.GenericoBean;
import com.sofactory.negocio.interfaces.AmigoBeanLocal;

@Stateless
@Local({AmigoBeanLocal.class})
public class AmigoBean extends GenericoBean<Amigo> implements AmigoBeanLocal{

	@PersistenceContext(unitName="MensajesPU")
	private EntityManager em;
	
	@PostConstruct
	private void iniciar(){
		super.em = this.em;
	}
	
	public List<Amigo> amigosDeUsuario(long codUser){
		
		Query query = em.createQuery("FROM com.sofactory.entidades.Amigo WHERE CODUSUARIO=:codUser ORDER BY CODAMIGO")
				.setParameter("codUser", codUser);
		List<Amigo> list = query.getResultList();
		
		return list;	
	}

	public boolean amigoDeUsuario(long codUser, long codAmigo){
		
		Query query = em.createQuery("FROM com.sofactory.entidades.Amigo WHERE CODUSUARIO=:codUser AND CODAMIGO=:codAmigo");
				query.setParameter("codUser", codUser);
				query.setParameter("codAmigo", codAmigo);
		List<Amigo> list = query.getResultList();
		
		return !list.isEmpty();	
	}
	
	public List<UsuarioDTO> encontrarUsuariosNoAmigos(String completar, Long usuario, int maximoRegistros){
		List<UsuarioDTO> usuarioDTOs = null;
		try {
			String sql = "SELECT p.*, u.FOTO FROM GU_PERSONA p JOIN GU_USUARIO u ON p.CODIGO = u.CODIGO WHERE (p.NOMBRES LIKE :filtro OR p.APELLIDOS LIKE :filtro) AND p.CODIGO <> :codigoUsuario AND p.CODIGO NOT IN (SELECT ma.CODAMIGO FROM ME_AMIGO ma WHERE ma.CODUSUARIO = :codigoUsuario)";
			Query query= em.createNativeQuery(sql);
			query.setParameter("filtro", "%"+completar+"%");
			query.setParameter("codigoUsuario", usuario);
			query.setMaxResults(maximoRegistros);
			List<Object[]> lista = query.getResultList();
			if (lista != null){
				usuarioDTOs = new ArrayList<UsuarioDTO>();
				for (Object[] u:lista){
					UsuarioDTO usuarioDTO = new UsuarioDTO();
					usuarioDTO.setCodigo(((BigInteger)u[5]).longValue());
					usuarioDTO.setNombres((String)u[4]);
					usuarioDTO.setApellidos((String)u[0]);
					usuarioDTO.setCorreo((String)u[2]);
					usuarioDTO.setFoto((byte[])u[7]);
					usuarioDTOs.add(usuarioDTO);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return usuarioDTOs;
	}
}