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
	
	// TODO: verificar a necessidade das datas de distribuição
	private TreeSet<Date> datasDistribuicaoFornecedor;
	
	private TreeSet<Date> datasDistribuicaoDistribuidor;
	
	private TreeSet<Date> datasDistribuicaoFornecedorDistribuidor;
	
	private Integer qtdDiasLimiteParaReprogLancamento;
	
	private boolean configuracaoInicial;

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
	 * @return the datasDistribuicaoFornecedor
	 */
	public TreeSet<Date> getDatasDistribuicaoFornecedor() {
		return datasDistribuicaoFornecedor;
	}

	/**
	 * @param datasDistribuicaoFornecedor the datasDistribuicaoFornecedor to set
	 */
	public void setDatasDistribuicaoFornecedor(
			TreeSet<Date> datasDistribuicaoFornecedor) {
		this.datasDistribuicaoFornecedor = datasDistribuicaoFornecedor;
	}

	/**
	 * @return the datasDistribuicaoDistribuidor
	 */
	public TreeSet<Date> getDatasDistribuicaoDistribuidor() {
		return datasDistribuicaoDistribuidor;
	}

	/**
	 * @param datasDistribuicaoDistribuidor the datasDistribuicaoDistribuidor to set
	 */
	public void setDatasDistribuicaoDistribuidor(
			TreeSet<Date> datasDistribuicaoDistribuidor) {
		this.datasDistribuicaoDistribuidor = datasDistribuicaoDistribuidor;
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
	
}
