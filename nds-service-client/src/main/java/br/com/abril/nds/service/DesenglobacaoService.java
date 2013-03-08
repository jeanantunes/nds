package br.com.abril.nds.service;

import br.com.abril.nds.model.distribuicao.Desenglobacao;

public interface DesenglobacaoService {
	
	void obterDesenglobacaoPorCota();
	
	void inserirDesenglobacao(Desenglobacao desenglobacao);

}
