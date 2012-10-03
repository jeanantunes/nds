package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
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
	 * @param dataOperacaoDistribuidor 
	 * 
	 * @return boolean
	 */
	boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor);
	
	/**
	 * Retorna uma lista com as notas fiscais de entrada que não tiveram seu recebimento fisico confirmado
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return List<ValidacaoRecebimentoFisicoFecharDiaDTO>
	 */
	List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor);
	
	/**
	 * Verifica se tem algum produto que não teve sua expedição confirmada.
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return boolean
	 */
	Boolean existeConfirmacaoDeExpedicao(Date dataOperacao);
	

	/**
	 * Retorna uma lista com os produtos que não tiveram sua expedição confirmada
	 * @param dataOperacaoDistribuidor 
	 * 
	 * 
	 * @return List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO>
	 */
	List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor);

}
