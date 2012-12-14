package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
	
	private Set<Date> datasExpectativaReparte;
	
	private BigInteger capacidadeDistribuicao;
	
	private TreeSet<Date> datasDistribuicaoFornecedor;
	
	private Integer qtdDiasLimiteParaReprogLancamento;
	
	private int numeroSemana;
	
	private Date dataLancamento;

	/**
	 * Construtor padrão.
	 */
	public DadosBalanceamentoLancamentoDTO() {
		
	}

	/**
	 * @return the capacidadeDistribuicao
	 */
	public BigInteger getCapacidadeDistribuicao() {
		return capacidadeDistribuicao;
	}

	/**
	 * @param capacidadeDistribuicao the capacidadeDistribuicao to set
	 */
	public void setCapacidadeDistribuicao(BigInteger capacidadeDistribuicao) {
		this.capacidadeDistribuicao = capacidadeDistribuicao;
	}

	/**
	 * @return the datasExpectativaReparte
	 */
	public Set<Date> getDatasExpectativaReparte() {
		return datasExpectativaReparte;
	}

	/**
	 * @param datasExpectativaReparte the datasExpectativaReparte to set
	 */
	public void setDatasExpectativaReparte(Set<Date> datasExpectativaReparte) {
		this.datasExpectativaReparte = datasExpectativaReparte;
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

	/**
	 * @return the dataLancamento
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
}
