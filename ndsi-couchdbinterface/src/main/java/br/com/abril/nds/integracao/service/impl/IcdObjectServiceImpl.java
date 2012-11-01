package br.com.abril.nds.integracao.service.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
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
	
	@Override
	public Set<Integer> recuperaSolicitacoesSolicitadas(String distribuidor) {

		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesSolicitadas() ;
	
	}

	@Override
	public Set<Integer> recuperaSolicitacoesAcertadas(String distribuidor) {
	
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesAcertadas();

	}

	@Override
	public List<SolicitacaoDTO> recuperaSolicitacoes(String distribuidor) {
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoes();
	}
}
