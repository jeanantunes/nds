package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * DTO com dados do balanceamento de lançamento.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoLancamentoDTO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4648494827298814073L;

	private TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento;
	
	private BigDecimal capacidadeDistribuicao;
	
	private int numeroSemana;
	
	private Date dataLancamento;
	
	private List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados;
	
	/**
	 * Construtor padrão.
	 */
	public BalanceamentoLancamentoDTO() {
		
	}

	/**
	 * @return the matrizLancamento
	 */
	public TreeMap<Date, List<ProdutoLancamentoDTO>> getMatrizLancamento() {
		return matrizLancamento;
	}

	/**
	 * @param matrizLancamento the matrizLancamento to set
	 */
	public void setMatrizLancamento(
			TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
		this.matrizLancamento = matrizLancamento;
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
	 * @return the produtosLancamentosCancelados
	 */
	public List<ProdutoLancamentoCanceladoDTO> getProdutosLancamentosCancelados() {
		return produtosLancamentosCancelados;
	}

	/**
	 * @param produtosLancamentosCancelados the produtosLancamentosCancelados to set
	 */
	public void setProdutosLancamentosCancelados(
			List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados) {
		this.produtosLancamentosCancelados = produtosLancamentosCancelados;
	}
	
}
