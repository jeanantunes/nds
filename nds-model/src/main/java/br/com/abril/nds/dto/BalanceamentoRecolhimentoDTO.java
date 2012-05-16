package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
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

	private BigDecimal capacidadeRecolhimentoDistribuidor;
	
	private boolean matrizFechada;
	
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
	 * @return the matrizFechada
	 */
	public boolean isMatrizFechada() {
		return matrizFechada;
	}

	/**
	 * @param matrizFechada the matrizFechada to set
	 */
	public void setMatrizFechada(boolean matrizFechada) {
		this.matrizFechada = matrizFechada;
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
	
}
