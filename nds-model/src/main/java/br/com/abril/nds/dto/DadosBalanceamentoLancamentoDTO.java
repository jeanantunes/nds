package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import br.com.abril.nds.vo.ConfirmacaoVO;

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
	
	private BigDecimal capacidadeDistribuicao;
	
	private TreeSet<Date> datasDistribuicaoFornecedorDistribuidor;
	
	private Integer qtdDiasLimiteParaReprogLancamento;
	
	private boolean configuracaoInicial;
	
	private int numeroSemana;
	
	private Date dataLancamento;
	
	private List<ConfirmacaoVO> datasConfirmacao;

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

	/**
	 * @return the datasConfirmacao
	 */
	public List<ConfirmacaoVO> getDatasConfirmacao() {
		return datasConfirmacao;
	}

	/**
	 * @param datasConfirmacao the datasConfirmacao to set
	 */
	public void setDatasConfirmacao(List<ConfirmacaoVO> datasConfirmacao) {
		this.datasConfirmacao = datasConfirmacao;
	}
	
}
