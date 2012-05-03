package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DTO que representa os dados referentes ao balanceamento do recolhimento. 
 * 
 * @author Discover Technology
 *
 */
public class RecolhimentoDTO implements Serializable {

	private static final long serialVersionUID = -2168766293591158494L;

	private List<ProdutoRecolhimentoDTO> listaProdutosRecolhimento;
	
	private Map<Date, BigDecimal> mapaQtdeExemplaresTotalDiaria;
	
	private Long capacidadeRecolhimentoDistribuidor;
	
	private List<Date> listaDiasRecolhimentoFornecedor;
	
	private List<Date> listaDiasRecolhimentoDistribuidor;

	/**
	 * Construtor padrão.
	 */
	public RecolhimentoDTO() {
		
	}

	/**
	 * @return the listaProdutosRecolhimento
	 */
	public List<ProdutoRecolhimentoDTO> getListaProdutosRecolhimento() {
		return listaProdutosRecolhimento;
	}

	/**
	 * @param listaProdutosRecolhimento the listaProdutosRecolhimento to set
	 */
	public void setListaProdutosRecolhimento(
			List<ProdutoRecolhimentoDTO> listaProdutosRecolhimento) {
		this.listaProdutosRecolhimento = listaProdutosRecolhimento;
	}

	/**
	 * @return the mapaQtdeExemplaresTotalDiaria
	 */
	public Map<Date, BigDecimal> getMapaQtdeExemplaresTotalDiaria() {
		return mapaQtdeExemplaresTotalDiaria;
	}

	/**
	 * @param mapaQtdeExemplaresTotalDiaria the mapaQtdeExemplaresTotalDiaria to set
	 */
	public void setMapaQtdeExemplaresTotalDiaria(
			Map<Date, BigDecimal> mapaQtdeExemplaresTotalDiaria) {
		this.mapaQtdeExemplaresTotalDiaria = mapaQtdeExemplaresTotalDiaria;
	}

	/**
	 * @return the capacidadeRecolhimentoDistribuidor
	 */
	public Long getCapacidadeRecolhimentoDistribuidor() {
		return capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @param capacidadeRecolhimentoDistribuidor the capacidadeRecolhimentoDistribuidor to set
	 */
	public void setCapacidadeRecolhimentoDistribuidor(
			Long capacidadeRecolhimentoDistribuidor) {
		this.capacidadeRecolhimentoDistribuidor = capacidadeRecolhimentoDistribuidor;
	}

	/**
	 * @return the listaDiasRecolhimentoFornecedor
	 */
	public List<Date> getListaDiasRecolhimentoFornecedor() {
		return listaDiasRecolhimentoFornecedor;
	}

	/**
	 * @param listaDiasRecolhimentoFornecedor the listaDiasRecolhimentoFornecedor to set
	 */
	public void setListaDiasRecolhimentoFornecedor(
			List<Date> listaDiasRecolhimentoFornecedor) {
		this.listaDiasRecolhimentoFornecedor = listaDiasRecolhimentoFornecedor;
	}

	/**
	 * @return the listaDiasRecolhimentoDistribuidor
	 */
	public List<Date> getListaDiasRecolhimentoDistribuidor() {
		return listaDiasRecolhimentoDistribuidor;
	}

	/**
	 * @param listaDiasRecolhimentoDistribuidor the listaDiasRecolhimentoDistribuidor to set
	 */
	public void setListaDiasRecolhimentoDistribuidor(
			List<Date> listaDiasRecolhimentoDistribuidor) {
		this.listaDiasRecolhimentoDistribuidor = listaDiasRecolhimentoDistribuidor;
	}
	
}
