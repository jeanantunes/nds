package br.com.abril.nds.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.FollowupNegociacaoRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.FollowupNegociacaoService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class FollowupNegociacaoServiceImpl implements FollowupNegociacaoService {

	@Autowired
	protected FollowupNegociacaoRepository followupChamadaoRepository;

	@Autowired
	protected NegociacaoDividaRepository negociacaoDividaRepository;
	
	@Autowired
	protected CobrancaService cobrancaService;
	
	@Override
	@Transactional(readOnly=true)
	public List<ConsultaFollowupNegociacaoDTO> obterNegociacoes(FiltroFollowupNegociacaoDTO filtro) {
		
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Negociação: Follow Up: Filtro não deve ser nulo.");
		
		return negociacaoDividaRepository.obterNegociacaoFollowup(filtro);		
	}
    
	@Transactional
	public void cancelarBaixaNegociacao(Long idNegociacao) {
		
		Long idCobranca = this.negociacaoDividaRepository.obterIdCobrancaPor(idNegociacao);
		
		this.cobrancaService.reverterBaixaManualDividas(Arrays.asList(idCobranca));
		
	}
	
	@Transactional(readOnly=true)
	public Long obterQuantidadeNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro){
		
		return negociacaoDividaRepository.obterQuantidadeNegociacaoFollowup(filtro);
	}
	
	
	
}
