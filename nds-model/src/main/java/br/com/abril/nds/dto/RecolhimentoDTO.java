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
public class RecolhimentoDTO implements Serializable {

	private static final long serialVersionUID = -2168766293591158494L;

	private List<ProdutoRecolhimentoDTO> produtosRecolhimento;
	
	private TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria;
	
	private BigDecimal capacidadeRecolhimentoDistribuidor;
	
	private TreeSet<Date> datasRecolhimentoFornecedor;
	
	private TreeSet<Date> datasRecolhimentoDistribuidor;
	
	private boolean semanaRecolhimento;
	
	private boolean configuracaoInicial;

	/**
	 * Construtor padrão.
	 */
	public RecolhimentoDTO() {
		
	}

	/**
	 * @return the capacidadeRecolhimentoDistribuidor
	 */
	public BigDecimal getCapacidadeRecolhimentoDistribuidor() {
		return capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to set
	 */
	public void setCapacidadeRecolhimentoDistribuidor(
			BigDecimal capacidadeRecolhimentoDistribuidor) {
		this.capacidadeRecolhimentoDistribuidor = capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @return the mapaExpectativaEncalheTotalDiaria
	 */
	public TreeMap<Date, BigDecimal> getMapaExpectativaEncalheTotalDiaria() {
		return mapaExpectativaEncalheTotalDiaria;
	}

	/**
	 * @param mapaExpectativaEncalheTotalDiaria the mapaExpectativaEncalheTotalDiaria to set
	 */
	public void setMapaExpectativaEncalheTotalDiaria(
			TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria) {
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
	 * @return the datasRecolhimentoDistribuidor
	 */
	public TreeSet<Date> getDatasRecolhimentoDistribuidor() {
		return datasRecolhimentoDistribuidor;
	}

	/**
	 * @param datasRecolhimentoDistribuidor the datasRecolhimentoDistribuidor to set
	 */
	public void setDatasRecolhimentoDistribuidor(
			TreeSet<Date> datasRecolhimentoDistribuidor) {
		this.datasRecolhimentoDistribuidor = datasRecolhimentoDistribuidor;
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
