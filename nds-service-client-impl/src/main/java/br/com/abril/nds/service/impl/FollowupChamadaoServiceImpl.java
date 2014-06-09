package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaFollowupChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.repository.FollowupChamadaoRepository;
import br.com.abril.nds.service.FollowupChamadaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Service
public class FollowupChamadaoServiceImpl implements FollowupChamadaoService {

	@Autowired
	private FollowupChamadaoRepository followupChamadaoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Override
	@Transactional
	public List<ConsultaFollowupChamadaoDTO>  obterConsignados(FiltroFollowupChamadaoDTO filtro) {

		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Chamadao: Follow Up: Filtro não deve ser nulo.");
		
		Distribuidor d = this.distribuidorService.obter();
				
		filtro.setQuantidadeDiasSuspenso(d.getPoliticaChamadao()!=null?d.getPoliticaChamadao().getDiasSuspenso().intValue():0);
		
		filtro.setValorConsignadoLimite(d.getPoliticaChamadao()!=null?d.getPoliticaChamadao().getValorConsignado():BigDecimal.ZERO);
		
		return this.followupChamadaoRepository.obterConsignadosParaChamadao(filtro);
	}

}
