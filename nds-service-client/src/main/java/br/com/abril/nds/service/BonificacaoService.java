package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.BonificacaoDTO;
import br.com.abril.nds.model.planejamento.Estudo;

public interface BonificacaoService {

	void salvarBonificacoes(Estudo estudo, List<BonificacaoDTO> bonificacaoDTOs);
	
}
