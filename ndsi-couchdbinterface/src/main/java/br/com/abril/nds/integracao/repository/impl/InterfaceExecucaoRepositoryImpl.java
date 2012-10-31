package br.com.abril.nds.integracao.repository.impl;

import javax.persistence.PersistenceException;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.integracao.persistence.model.InterfaceExecucao;
import br.com.abril.nds.integracao.repository.InterfaceExecucaoRepository;


@Repository
public class InterfaceExecucaoRepositoryImpl extends AbstractRepositoryModel<InterfaceExecucao, Long>
		implements InterfaceExecucaoRepository {

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
