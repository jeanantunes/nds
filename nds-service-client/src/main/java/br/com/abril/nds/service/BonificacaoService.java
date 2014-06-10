package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.model.planejamento.EstudoGerado;

public interface BonificacaoService {

	void salvarBonificacoes(EstudoGerado estudo, List<BonificacaoDTO> bonificacaoDTOs);
	
}
