package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<ConsultaFollowupNegociacaoDTO> obterNegociacoes(FiltroFollowupNegociacaoDTO filtro) {
		
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Negociação: Follow Up: Filtro não deve ser nulo.");
		
		ConsultaFollowupNegociacaoDTO dto = new ConsultaFollowupNegociacaoDTO();
		dto.setDataVencimento(new Date());
		dto.setDescricaoFormaPagamento("Forma pagamento");
		dto.setDescricaoParcelamento("1/2");
		dto.setNomeJornaleiro("mane");
		dto.setNumeroCota(123L);
		
		List<ConsultaFollowupNegociacaoDTO> listanegociacao = new ArrayList<ConsultaFollowupNegociacaoDTO>();
						
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		listanegociacao.add(dto);
		
		return listanegociacao;		
	}

}
