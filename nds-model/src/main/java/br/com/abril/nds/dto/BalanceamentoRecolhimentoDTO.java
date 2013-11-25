<<<<<<< HEAD
package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * DTO com dados do balanceamento de recolhimento.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4498486203318581867L;
	
	private TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento;

	private BigInteger capacidadeRecolhimentoDistribuidor;
	
	private List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados;
	
	private List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada;
	
	private List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados;
	
	/**
	 * Construtor padrão.
	 */
	public BalanceamentoRecolhimentoDTO() {
		
	}

	/**
	 * @return the matrizRecolhimento
	 */
	public TreeMap<Date, List<ProdutoRecolhimentoDTO>> getMatrizRecolhimento() {
		return matrizRecolhimento;
	}

	/**
	 * @param matrizRecolhimento the matrizRecolhimento to set
	 */
	public void setMatrizRecolhimento(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		this.matrizRecolhimento = matrizRecolhimento;
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
	 * @return the produtosRecolhimentoNaoBalanceados
	 */
	public List<ProdutoRecolhimentoDTO> getProdutosRecolhimentoNaoBalanceados() {
		return produtosRecolhimentoNaoBalanceados;
	}

	/**
	 * @param produtosRecolhimentoNaoBalanceados the produtosRecolhimentoNaoBalanceados to set
	 */
	public void setProdutosRecolhimentoNaoBalanceados(
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados) {
		this.produtosRecolhimentoNaoBalanceados = produtosRecolhimentoNaoBalanceados;
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
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * DTO com dados do balanceamento de recolhimento.
 * 
 * @author Discover Technology
 *
 */
public class BalanceamentoRecolhimentoDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4498486203318581867L;
	
	private TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento;

	private BigInteger capacidadeRecolhimentoDistribuidor;
	
	/**
	 * Construtor padrão.
	 */
	public BalanceamentoRecolhimentoDTO() {
		
	}

	/**
	 * @return the matrizRecolhimento
	 */
	public TreeMap<Date, List<ProdutoRecolhimentoDTO>> getMatrizRecolhimento() {
		return matrizRecolhimento;
	}

	/**
	 * @param matrizRecolhimento the matrizRecolhimento to set
	 */
	public void setMatrizRecolhimento(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		this.matrizRecolhimento = matrizRecolhimento;
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
	
}
>>>>>>> fase2
