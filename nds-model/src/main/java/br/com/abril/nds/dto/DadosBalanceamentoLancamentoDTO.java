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
	
	private TreeSet<Date> datasDistribuicaoFornecedor;
	
	private TreeSet<Date> datasDistribuicaoDistribuidor;
	
	// TODO: private boolean balancearMatriz;
	
	// TODO: private boolean matrizFechada;

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
	
}
