package br.com.abril.nds.integracao.repository.impl;

import javax.persistence.PersistenceException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.repository.InterfaceExecucaoRepository;
import br.com.abril.nds.model.integracao.InterfaceExecucao;


@Repository
@Transactional("transactionManager")
public class InterfaceExecucaoRepositoryImpl extends AbstractRepositoryModel<InterfaceExecucao, Long> implements InterfaceExecucaoRepository {

	public InterfaceExecucaoRepositoryImpl() {
		super(InterfaceExecucao.class);
	}
	
	public InterfaceExecucao findById(Long codigoInterface) {
		
		try {
			return (InterfaceExecucao) getSession().get(InterfaceExecucao.class, codigoInterface);
		} catch (PersistenceException e) {
			return null;
		}
	}
}
