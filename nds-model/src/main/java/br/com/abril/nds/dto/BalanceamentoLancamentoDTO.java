package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
	
	private BigInteger capacidadeDistribuicao;
	
	private Date dataLancamento;
	
	private List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados;
	
	private Set<Date> datasExpedicaoConfirmada;
	
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

	/**
	 * @return the datasExpedicaoConfirmada
	 */
	public Set<Date> getDatasExpedicaoConfirmada() {
		return datasExpedicaoConfirmada;
	}

	/**
	 * @param datasExpedicaoConfirmada the datasExpedicaoConfirmada to set
	 */
	public void setDatasExpedicaoConfirmada(Set<Date> datasExpedicaoConfirmada) {
		this.datasExpedicaoConfirmada = datasExpedicaoConfirmada;
	}
	
}
