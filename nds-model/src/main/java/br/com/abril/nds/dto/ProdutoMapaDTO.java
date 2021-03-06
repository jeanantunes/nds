package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProdutoMapaDTO implements Serializable{
	
	private static final long serialVersionUID = 5959348243641631020L;

	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private String codigoDeBarras;
	private String precoCapa;
	private Integer totalReparte;
	private Map<Integer, Integer> boxQtde;
	
	public ProdutoMapaDTO(){
		
	}
	
	public ProdutoMapaDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String codigoDeBarras, String precoCapa, Integer totalReparte,
			Map<Integer, Integer> qtdeBox) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.codigoDeBarras = codigoDeBarras;
		this.precoCapa = precoCapa;
		this.totalReparte = totalReparte;
		this.boxQtde = qtdeBox;
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
	 * @return the totalReparte
	 */
	public Integer getTotalReparte() {
		return totalReparte;
	}
	/**
	 * @param totalReparte the totalReparte to set
	 */
	public void setTotalReparte(Integer totalReparte) {
		this.totalReparte = totalReparte;
	}
	/**
	 * @return the boxQtde
	 */
	public Map<Integer, Integer> getBoxQtde() {
		return boxQtde;
	}
	/**
	 * @param boxQtde the boxQtde to set
	 */
	public void setBoxQtde(HashMap<Integer, Integer> boxQtde) {
		this.boxQtde = boxQtde;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}
	
}