package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;

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
	
	/**
	 * Retorna uma lista com as notas fiscais de entrada que não tiveram seu recebimento fisico confirmado
	 * 
	 * 
	 * @return boolean
	 */
	List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado();

}
