package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.filtro.FiltroControleAprovacaoDTO;
import br.com.abril.nds.model.aprovacao.Aprovacao;
import br.com.abril.nds.service.ControleAprovacaoService;

/**
 * Classe de implementação de serviços referentes à
 * tela de controle de aprovações.
 * 
 * @author Discover Technology
 */
@Service
public class ControleAprovacaoServiceImpl implements ControleAprovacaoService {

	@Override
	@Transactional(readOnly = true)
	public List<Aprovacao> obterAprovacoes(FiltroControleAprovacaoDTO filtro) {
		
		return null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterTotalAprovacoes(FiltroControleAprovacaoDTO filtro) {
		
		return null;
	}
	
}
