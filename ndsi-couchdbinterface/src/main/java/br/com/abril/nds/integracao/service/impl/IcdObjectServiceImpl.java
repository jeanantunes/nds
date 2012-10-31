package br.com.abril.nds.integracao.service.impl;

import java.util.Set;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import br.com.abril.nds.integracao.icd.model.IcdObject;
import br.com.abril.nds.integracao.model.InterfaceExecucao;
import br.com.abril.nds.integracao.repository.InterfaceExecucaoRepository;
import br.com.abril.nds.integracao.repository.SolicitacaoFaltasSobrasRepository;
import br.com.abril.nds.integracao.service.IcdObjectService;


@Service
public class IcdObjectServiceImpl implements IcdObjectService {
	
	@Autowired
	private SolicitacaoFaltasSobrasRepository solicitacaoFaltasSobrasRepository;
	

	public IcdObjectServiceImpl() {
		
	}
	
	public Set<Integer> recuperaSolicitacoesSolicitadas() {
		
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesSolicitadas() ;
		
	}
	
	public Set<Integer> recuperaSolicitacoesAcertadas() {
		
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesAcertadas();
		
	}
}
