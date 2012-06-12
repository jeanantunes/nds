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
	private static final long serialVersionUID = -4498486203318581867L;
	
	private TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento;
	
	private BigDecimal capacidadeDistribuicao;
	
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
	
}
