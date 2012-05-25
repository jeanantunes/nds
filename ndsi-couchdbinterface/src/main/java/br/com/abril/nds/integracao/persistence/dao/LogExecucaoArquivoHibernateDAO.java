package br.com.abril.nds.integracao.persistence.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.persistence.model.LogExecucaoArquivo;

@Component("logExecucaoArquivoDAO")
public class LogExecucaoArquivoHibernateDAO {

	@PersistenceContext
	private EntityManager em;
	
	public LogExecucaoArquivo inserir(LogExecucaoArquivo logExecucaoArquivo) {
		em.persist(logExecucaoArquivo);
		em.flush();
		return logExecucaoArquivo;
	}
}
