package br.com.abril.nds.integracao.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.persistence.model.InterfaceExecucao;

@Component("interfaceExecucaoDAO")
public class InterfaceExecucaoHibernateDAO {

	@PersistenceContext
	private EntityManager em;
	
	public InterfaceExecucao findById(Long codigoInterface) {
		
		try {
			return em.find(InterfaceExecucao.class, codigoInterface);
		} catch (PersistenceException e) {
			return null;
		}
	}
}
