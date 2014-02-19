package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.service.NaturezaOperacaoService;

@Service
public class NaturezaOperacaoServiceImpl implements NaturezaOperacaoService {
	
	@Autowired
	private NaturezaOperacaoRepository naturezaOperacaoRepository;
	
	@Override
	@Transactional
	public NaturezaOperacao obterNaturezaOperacaoPorId(Long idNaturezaOperacao) {
		return naturezaOperacaoRepository.buscarPorId(idNaturezaOperacao);
	}
	
}