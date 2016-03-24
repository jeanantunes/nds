package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.util.TipoBaixaCobranca;

/**
 * Interface para reger a lógica de negócio de Acumulo de Dívidas.  
 * 
 * @author Discover Technology
 *
 */
public interface AcumuloDividasService {
	
	/**
	 * Cria um novo acúmulo de dívidas.
	 * 
	 * @param acumuloDivida
	 */
	void criarAcumuloDivida(AcumuloDivida acumuloDivida);
	
	/**
	 * Atualiza um acúmulo de dívidas.
	 * 
	 * @param acumuloDivida
	 * 
	 * @return {@link AcumuloDivida}
	 */
	AcumuloDivida atualizarAcumuloDivida(AcumuloDivida acumuloDivida);
	
	/**
	 * Obtém uma acumulo através do ID de um {@link br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota} 
	 * 
	 * @param idMovimentoFinanceiro
	 * 
	 * @return {@link AcumuloDivida}
	 */
	AcumuloDivida obterAcumuloDividaPorMovimentoPendente(Long idMovimentoFinanceiro);

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
	 * @param tipoBaixa - tipo da baixa que está sendo realizadas
	 * 
	 * @return Número de acúmulos da dívida 
	 */
	BigInteger obterNumeroDeAcumulosDivida(Long idConsolidadoFinanceiroCota);

	void quitarDividasAcumuladas(Date dataPagamento, Divida dividaAtual,TipoBaixaCobranca tipoBaixa);
}
