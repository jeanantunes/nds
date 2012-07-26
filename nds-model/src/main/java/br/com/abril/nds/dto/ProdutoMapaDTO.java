package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.HashMap;

public class ProdutoMapaDTO implements Serializable{
	
	private static final long serialVersionUID = 5959348243641631020L;

	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private String precoCapa;
	private Integer totalReparte;
	private HashMap<Integer, Integer> boxQtde;
	
	public ProdutoMapaDTO(){
		
	}
	
	public ProdutoMapaDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String precoCapa, Integer totalReparte,
			 HashMap<Integer, Integer> qtdeBox) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
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
	public HashMap<Integer, Integer> getBoxQtde() {
		return boxQtde;
	}
	/**
	 * @param boxQtde the boxQtde to set
	 */
	public void setBoxQtde(HashMap<Integer, Integer> boxQtde) {
		this.boxQtde = boxQtde;
	}
	
	
}
