package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.FollowupNegociacaoRepository;
import br.com.abril.nds.service.FollowupNegociacaoService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class FollowupNegociacaoServiceImpl implements FollowupNegociacaoService {

	@Autowired
	protected FollowupNegociacaoRepository followupChamadaoRepository;

	@Override
	public List<ConsultaFollowupNegociacaoDTO> obterNegociacoes(
			FiltroFollowupNegociacaoDTO filtro) {
		
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Negociação: Follow Up: Filtro não deve ser nulo.");
		
		List<ConsultaFollowupNegociacaoDTO> listanegociacao =
		this.followupChamadaoRepository.obterConsignadosParaChamadao(filtro);
	
		return listanegociacao;		
	}

}
