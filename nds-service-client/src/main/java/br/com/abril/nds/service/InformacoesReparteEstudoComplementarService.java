package br.com.abril.nds.service;

import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.planejamento.InformacoesReparteComplementarEstudo;

public interface InformacoesReparteEstudoComplementarService {

	void salvarInformacoes(InformacoesReparteComplementarEstudo informacoes);
	
}
