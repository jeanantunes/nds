package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Map;

public class MapaProdutoCotasDTO implements Serializable{
	
	private static final long serialVersionUID = -8389694835840404984L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private String precoCapa;
	private Map<Integer, Integer> cotasQtdes;
	
	public MapaProdutoCotasDTO() {
		
	}

	public MapaProdutoCotasDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String precoCapa,
			Map<Integer, Integer> cotasQtdes) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.precoCapa = precoCapa;
		this.setCotasQtdes(cotasQtdes);
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the precoCapa
	 */
	public String getPrecoCapa() {
		return precoCapa;
	}

	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}

	public Map<Integer, Integer> getCotasQtdes() {
		return cotasQtdes;
	}



	public void setCotasQtdes(Map<Integer, Integer> cotasQtdes) {
		this.cotasQtdes = cotasQtdes;
	}
	
}
