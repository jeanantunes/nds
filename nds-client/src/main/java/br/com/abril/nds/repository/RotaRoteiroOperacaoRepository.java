package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;

public interface RotaRoteiroOperacaoRepository {
	
	/**
	 * Obtem o roteiro de impressão de dividas de uma determinada cota. 
	 * @param numeroCota
	 * @return
	 */
	RotaRoteiroOperacao obterRoterioImpressaoDividaCota(Integer numeroCota);
}
