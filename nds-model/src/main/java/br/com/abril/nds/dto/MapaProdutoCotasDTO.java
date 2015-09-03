package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapaProdutoCotasDTO implements Serializable{
	
	private static final long serialVersionUID = -8389694835840404984L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private String codigoDeBarras;
	private String precoCapa;
	private Integer qtdes;
	private Integer sequenciaMatriz;
	private Integer pacotePadrao;
	private Map<Integer, Integer> cotasQtdes;
	private Map<String, Integer> boxQtdes;
	
	public MapaProdutoCotasDTO() {
		
	}

	public MapaProdutoCotasDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String codigoDeBarras, String precoCapa,
			Map<Integer, Integer> cotasQtdes, Map<String, Integer> boxQtdes) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.codigoDeBarras = codigoDeBarras;
		this.precoCapa = precoCapa;
		this.setCotasQtdes(cotasQtdes);
		this.boxQtdes = boxQtdes;
	}
	

	public MapaProdutoCotasDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String codigoDeBarras, String precoCapa,
			Integer sequenciaMatriz,
			Integer pacotePadrao,
			LinkedHashMap<Integer, Integer> cotasQtdes,
			LinkedHashMap<String, Integer> boxQtdes) {
		super();
		
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.codigoDeBarras = codigoDeBarras;
		this.precoCapa = precoCapa;
		this.setCotasQtdes(cotasQtdes);
		this.sequenciaMatriz = sequenciaMatriz;
		this.pacotePadrao = pacotePadrao;
		this.boxQtdes = boxQtdes;
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

	/**
	 * @return the boxQtdes
	 */
	public Map<String, Integer> getBoxQtdes() {
		return boxQtdes;
	}

	/**
	 * @param boxQtdes the boxQtdes to set
	 */
	public void setBoxQtdes(Map<String, Integer> boxQtdes) {
		this.boxQtdes = boxQtdes;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public Integer getQtdes() {
		return qtdes;
	}

	public void setQtdes(Integer qtdes) {
		this.qtdes = qtdes;
	}

	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}

	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
}