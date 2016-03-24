package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.model.financeiro.Cobranca;

/**
 * Repositório de dados referentes ao acumulo de dívidas.
 * 
 * @author Discover Technology
 *
 */
public interface AcumuloDividasRepository extends Repository<AcumuloDivida, Long> {

	/**
	 * Obtém uma acumulo através do ID de um {@link br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota} 
	 * 
	 * @param idMovimentoFinanceiro
	 * 
	 * @return {@link AcumuloDivida}
	 */
	AcumuloDivida obterAcumuloDividaPorMovimentoFinanceiroPendente(Long idMovimentoFinanceiro);
	
	/**
	 * Obtém uma lista de acumulo através do ID de um {@link br.com.abril.nds.model.financeiro.Divida} 
	 * 
	 * @param idDivida
	 * 
	 * @return {@link AcumuloDivida}
	 */
	List<AcumuloDivida> obterAcumuloDividaPorDivida(Long idDivida);
	
	/**
	 * Obtém o número máximo de acúmulos já atingido por determinada Cota.
	 * 
	 * @param idCota - ID da cota a ser pesquisado.
	 * 
	 * @return - Número de maior acúmulo de suas dívidas.
	 */
	BigInteger obterNumeroMaximoAcumuloCota(Long idCota, Long idDivida);
	
	/**
	 * Obtém o número atual de acúmulos de dívida da cota.
	 * 
	 * @param idConsolidadoFinanceiroCota - id do consolidado financeiro da cota.
	 * 
	 * @return Número de acúmulos da dívida 
	 */
	BigInteger obterNumeroDeAcumulosDivida(Long idConsolidadoFinanceiroCota);

    List<AcumuloDivida> buscarDividasPendentesIndimplencia(Date dataCriacao, Cobranca cobranca);
	
}
