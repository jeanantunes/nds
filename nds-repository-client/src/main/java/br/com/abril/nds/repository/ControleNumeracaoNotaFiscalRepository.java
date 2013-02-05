package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.ControleNumeracaoNotaFiscal;

public interface ControleNumeracaoNotaFiscalRepository extends Repository<ControleNumeracaoNotaFiscal, Long> {

	public ControleNumeracaoNotaFiscal obterControleNumeracaoNotaFiscal(String serieNF);
	
	public ControleNumeracaoNotaFiscal obterForUpdateControleNumeracaoNotaFiscal(Long idControleNumeracaoNotaFiscal);

	
}
