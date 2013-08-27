package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;

public class ProdutoMapaCotaDTO implements Serializable{

	private static final long serialVersionUID = -203864073274071280L;
	
	private String nomeProduto;
	private Long numeroEdicao;
	private Integer sm;
	private String precoCapa;
	private Integer total;
	private List<Cota> cotas;
	
	
	public ProdutoMapaCotaDTO(){
	
	}
	
	public ProdutoMapaCotaDTO(String nomeProduto, Long numeroEdicao,
		Integer sm, String precoCapa, Integer total) {
		super();
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.sm = sm;
		this.precoCapa = precoCapa;
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
	
	public List<Cota> getCotas() {
		return cotas;
	}
	
	public void setCotas(List<Cota> cotas) {
		this.cotas = cotas;
	}


}