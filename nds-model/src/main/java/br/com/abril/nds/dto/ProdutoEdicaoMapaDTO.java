package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.HashMap;

public class ProdutoEdicaoMapaDTO implements Serializable {

	private static final long serialVersionUID = 3273941225609177113L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private String codigoDeBarras;
	private String precoCapa;
	private HashMap<String, BoxRotasDTO> boxes;
	private Integer repartePromocional;
	
	public ProdutoEdicaoMapaDTO() {
		
	}
	
	public ProdutoEdicaoMapaDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String codigoDeBarras, String precoCapa, HashMap<String, BoxRotasDTO> boxes,
			Integer repartePromocional) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.codigoDeBarras = codigoDeBarras;
		this.precoCapa = precoCapa;
		this.boxes = boxes;
		this.repartePromocional = repartePromocional;
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

	/**
	 * @return the box
	 */
	public HashMap<String, BoxRotasDTO> getBoxes() {
		return boxes;
	}

	/**
	 * @param box the box to set
	 */
	public void setBoxes(HashMap<String, BoxRotasDTO> boxes) {
		this.boxes = boxes;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public Integer getRepartePromocional() {
		return repartePromocional;
	}

	public void setRepartePromocional(Integer repartePromocional) {
		this.repartePromocional = repartePromocional;
	}
	
}