package br.com.abril.nds.dto;

import java.io.Serializable;

public class ProdutoMapaCotaDTO implements  Serializable{

	private static final long serialVersionUID = -203864073274071280L;

	private String nomeProduto;
	private Long numeroEdicao;
	private Integer sm;
	private Integer total;
	
	public ProdutoMapaCotaDTO(){
		
	}
	
	public ProdutoMapaCotaDTO(String nomeProduto, Long numeroEdicao,
			Integer sm, Integer total) {
		super();
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.sm = sm;
		this.total = total;
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
	 * @return the sm
	 */
	public Integer getSm() {
		return sm;
	}
	/**
	 * @param sm the sm to set
	 */
	public void setSm(Integer sm) {
		this.sm = sm;
	}
	/**
	 * @return the total
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}
