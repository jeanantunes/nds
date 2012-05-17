package br.com.abril.nds.integracao.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.persistence.model.LogExecucao;

@Component("logExecucaoDAO")
public class LogExecucaoHibernateDAO {

	@PersistenceContext
	private EntityManager em;
	
	public void logInicio(LogExecucao logExecucao) {
		em.persist(logExecucao);
	}
}
