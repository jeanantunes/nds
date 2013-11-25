<<<<<<< HEAD
package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
	
	private TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria;
	
	private BigInteger capacidadeRecolhimentoDistribuidor;
	
	private TreeSet<Date> datasRecolhimentoFornecedor;
	
	private boolean forcarBalanceamento;
	
	private List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada;
	
	private List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados;

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

	/**
	 * @return the cotasOperacaoDiferenciada
	 */
	public List<CotaOperacaoDiferenciadaDTO> getCotasOperacaoDiferenciada() {
		return cotasOperacaoDiferenciada;
	}

	/**
	 * @param cotasOperacaoDiferenciada the cotasOperacaoDiferenciada to set
	 */
	public void setCotasOperacaoDiferenciada(
		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {
		this.cotasOperacaoDiferenciada = cotasOperacaoDiferenciada;
	}

	/**
	 * @return the produtosRecolhimentoAgrupados
	 */
	public List<ProdutoRecolhimentoDTO> getProdutosRecolhimentoAgrupados() {
		return produtosRecolhimentoAgrupados;
	}

	/**
	 * @param produtosRecolhimentoAgrupados the produtosRecolhimentoAgrupados to set
	 */
	public void setProdutosRecolhimentoAgrupados(
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados) {
		this.produtosRecolhimentoAgrupados = produtosRecolhimentoAgrupados;
	}
	
}
=======
package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
	
	private TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria;
	
	private BigInteger capacidadeRecolhimentoDistribuidor;
	
	private TreeSet<Date> datasRecolhimentoFornecedor;
	
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
>>>>>>> fase2
