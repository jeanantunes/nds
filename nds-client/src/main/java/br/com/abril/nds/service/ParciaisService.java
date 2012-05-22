package br.com.abril.nds.service;

import java.util.Date;



/**
 * Interface que define os serviços referentes a Parciais
 * 
 * @author guilherme.morais
 *
 */
public interface ParciaisService {

	/**
	 * Cria novos período parcial para determinada edição de um produto 
	 * 
	 * @param idProdutoEdicao - Id do ProdutoEdicao
	 * @param qtdePeriodos - Quantidade de períodos
	 * @param idUsuario - Id do Usuário
	 * @param peb - periodo em banca (dias) - caso nulo, será obtido do ProdutoEdicao 
	 */
	void gerarPeriodosParcias(Long idProdutoEdicao, Integer qtdePeriodos,Long idUsuario, Integer peb);

	/**
	 * Altera data de Período de Lancamento Parcial
	 * 
	 * @param idLancamento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 */
	void alterarPeriodo(Long idLancamento, Date dataLancamento,
			Date dataRecolhimento);

	/**
	 * Remove PeriodoLancamentoParcial e Lancamento referente ao mesmo
	 * 
	 * @param idLancamento
	 */
	void excluirPeriodo(Long idLancamento);

}
