package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * DTO que representa os dados referentes ao balanceamento do recolhimento. 
 * 
 * @author Discover Technology
 *
 */
public class DadosBalanceamentoLancamentoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4146330226397521701L;

	private List<ProdutoLancamentoDTO> produtosLancamento;
	
	private TreeMap<Date, BigDecimal> mapaExpectativaReparteTotalDiario;
	
	private BigDecimal capacidadeDistribuicao;
	
	private TreeSet<Date> datasDistribuicaoFornecedorDistribuidor;
	
	private Integer qtdDiasLimiteParaReprogLancamento;
	
	private boolean configuracaoInicial;
	
	private int numeroSemana;

	/**
	 * Construtor padrão.
	 */
	public DadosBalanceamentoLancamentoDTO() {
		
	}

	/**
	 * @return the capacidadeDistribuicao
	 */
	public BigDecimal getCapacidadeDistribuicao() {
		return capacidadeDistribuicao;
	}

	/**
	 * @param capacidadeDistribuicao the capacidadeDistribuicao to set
	 */
	public void setCapacidadeDistribuicao(BigDecimal capacidadeDistribuicao) {
		this.capacidadeDistribuicao = capacidadeDistribuicao;
	}

	/**
	 * @return the mapaExpectativaReparteTotalDiario
	 */
	public TreeMap<Date, BigDecimal> getMapaExpectativaReparteTotalDiario() {
		return mapaExpectativaReparteTotalDiario;
	}

	/**
	 * @param mapaExpectativaReparteTotalDiario the mapaExpectativaReparteTotalDiario to set
	 */
	public void setMapaExpectativaReparteTotalDiario(
			TreeMap<Date, BigDecimal> mapaExpectativaReparteTotalDiario) {
		this.mapaExpectativaReparteTotalDiario = mapaExpectativaReparteTotalDiario;
	}

	/**
	 * @return the produtosLancamento
	 */
	public List<ProdutoLancamentoDTO> getProdutosLancamento() {
		return produtosLancamento;
	}

	/**
	 * @param produtosLancamento the produtosLancamento to set
	 */
	public void setProdutosLancamento(List<ProdutoLancamentoDTO> produtosLancamento) {
		this.produtosLancamento = produtosLancamento;
	}

	/**
	 * @return the datasDistribuicaoFornecedorDistribuidor
	 */
	public TreeSet<Date> getDatasDistribuicaoFornecedorDistribuidor() {
		return datasDistribuicaoFornecedorDistribuidor;
	}

	/**
	 * @param datasDistribuicaoFornecedorDistribuidor the datasDistribuicaoFornecedorDistribuidor to set
	 */
	public void setDatasDistribuicaoFornecedorDistribuidor(
			TreeSet<Date> datasDistribuicaoFornecedorDistribuidor) {
		this.datasDistribuicaoFornecedorDistribuidor = datasDistribuicaoFornecedorDistribuidor;
	}

	/**
	 * @return the qtdDiasLimiteParaReprogLancamento
	 */
	public Integer getQtdDiasLimiteParaReprogLancamento() {
		return qtdDiasLimiteParaReprogLancamento;
	}

	/**
	 * @param qtdDiasLimiteParaReprogLancamento the qtdDiasLimiteParaReprogLancamento to set
	 */
	public void setQtdDiasLimiteParaReprogLancamento(
			Integer qtdDiasLimiteParaReprogLancamento) {
		this.qtdDiasLimiteParaReprogLancamento = qtdDiasLimiteParaReprogLancamento;
	}

	/**
	 * @return the configuracaoInicial
	 */
	public boolean isConfiguracaoInicial() {
		return configuracaoInicial;
	}

	/**
	 * @param configuracaoInicial the configuracaoInicial to set
	 */
	public void setConfiguracaoInicial(boolean configuracaoInicial) {
		this.configuracaoInicial = configuracaoInicial;
	}

	/**
	 * @return the numeroSemana
	 */
	public int getNumeroSemana() {
		return numeroSemana;
	}

	/**
	 * @param numeroSemana the numeroSemana to set
	 */
	public void setNumeroSemana(int numeroSemana) {
		this.numeroSemana = numeroSemana;
	}
	
}
