package br.com.abril.nds.service;

import java.util.Date;

public interface FecharDiaService {
	
	/**
	 * Verifica se tem cobrança para o dia(D-1) de operação do distribuidor.
	 * 
	 * @param dataOperacaoDistribuidor
	 * @return
	 */
	boolean existeCobrancaParaFecharDia(Date dataOperacaoDistribuidor);
	
	/**
	 * Verifica se tem nota fiscal com recebimento lógico mas não tem recebimento fisíco
	 * 
	 * @return boolean
	 */
	boolean existeNotaFiscalSemRecebimentoFisico();

}
