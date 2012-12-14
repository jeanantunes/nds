package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
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
public class RecolhimentoDTO implements Serializable {

	private static final long serialVersionUID = -2168766293591158494L;

	private List<ProdutoRecolhimentoDTO> produtosRecolhimento;
	
	private TreeMap<Date, BigInteger> mapaExpectativaEncalheTotalDiaria;
	
	private BigInteger capacidadeRecolhimentoDistribuidor;
	
	private TreeSet<Date> datasRecolhimentoFornecedor;
	
	private boolean semanaRecolhimento;
	
	private boolean forcarBalanceamento;

	/**
	 * Construtor padrão.
	 */
	public RecolhimentoDTO() {
		
	}

	/**
	 * @return the capacidadeRecolhimentoDistribuidor
	 */
	public BigInteger getCapacidadeRecolhimentoDistribuidor() {
		return capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to set
	 */
	public void setCapacidadeRecolhimentoDistribuidor(
			BigInteger capacidadeRecolhimentoDistribuidor) {
		this.capacidadeRecolhimentoDistribuidor = capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @return the mapaExpectativaEncalheTotalDiaria
	 */
	public TreeMap<Date, BigInteger> getMapaExpectativaEncalheTotalDiaria() {
		return mapaExpectativaEncalheTotalDiaria;
	}

	/**
	 * @param mapaExpectativaEncalheTotalDiaria the mapaExpectativaEncalheTotalDiaria to set
	 */
	public void setMapaExpectativaEncalheTotalDiaria(
			TreeMap<Date, BigInteger> mapaExpectativaEncalheTotalDiaria) {
		this.mapaExpectativaEncalheTotalDiaria = mapaExpectativaEncalheTotalDiaria;
	}

	/**
	 * @return the produtosRecolhimento
	 */
	public List<ProdutoRecolhimentoDTO> getProdutosRecolhimento() {
		return produtosRecolhimento;
	}

	/**
	 * @param produtosRecolhimento the produtosRecolhimento to set
	 */
	public void setProdutosRecolhimento(
			List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		this.produtosRecolhimento = produtosRecolhimento;
	}

	/**
	 * @return the datasRecolhimentoFornecedor
	 */
	public TreeSet<Date> getDatasRecolhimentoFornecedor() {
		return datasRecolhimentoFornecedor;
	}

	/**
	 * @param datasRecolhimentoFornecedor the datasRecolhimentoFornecedor to set
	 */
	public void setDatasRecolhimentoFornecedor(
			TreeSet<Date> datasRecolhimentoFornecedor) {
		this.datasRecolhimentoFornecedor = datasRecolhimentoFornecedor;
	}

	/**
	 * @return the semanaRecolhimento
	 */
	public boolean isSemanaRecolhimento() {
		return semanaRecolhimento;
	}

	/**
	 * @param semanaRecolhimento the semanaRecolhimento to set
	 */
	public void setSemanaRecolhimento(boolean semanaRecolhimento) {
		this.semanaRecolhimento = semanaRecolhimento;
	}

	/**
	 * @return the forcarBalanceamento
	 */
	public boolean isForcarBalanceamento() {
		return forcarBalanceamento;
	}

	/**
	 * @param forcarBalanceamento the forcarBalanceamento to set
	 */
	public void setForcarBalanceamento(boolean forcarBalanceamento) {
		this.forcarBalanceamento = forcarBalanceamento;
	}
	
}
