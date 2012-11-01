package br.com.abril.nds.integracao.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.repository.SolicitacaoFaltasSobrasRepository;
import br.com.abril.nds.integracao.service.IcdObjectService;


@Service
public class IcdObjectServiceImpl implements IcdObjectService {
	
	@Autowired
	private SolicitacaoFaltasSobrasRepository solicitacaoFaltasSobrasRepository;
	

	public IcdObjectServiceImpl() {
		
	}
	
	@Override
	public Set<Integer> recuperaSolicitacoesSolicitadas(Integer distribuidor) {

		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesSolicitadas(distribuidor) ;
	
	}

	@Override
	public Set<Integer> recuperaSolicitacoesAcertadas(Integer distribuidor) {
	
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesAcertadas(distribuidor);

	}

	@Override
	public List<SolicitacaoDTO> recuperaSolicitacoes(Integer distribuidor) {
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoes(distribuidor);
	}
}
