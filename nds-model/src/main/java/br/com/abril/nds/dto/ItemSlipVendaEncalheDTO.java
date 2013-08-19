package br.com.abril.nds.dto;

import java.io.Serializable;

public class ItemSlipVendaEncalheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//ITEM SLIP
	private String codigo;
	private String produto;
	private String edicao;
	private String preco;
	private String quantidade;
	private String total;
	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the produto
	 */
	public String getProduto() {
		return produto;
	}
	/**
	 * @param produto the produto to set
	 */
	public void setProduto(String produto) {
		this.produto = produto;
	}
	/**
	 * @return the edicao
	 */
	public String getEdicao() {
		return edicao;
	}
	/**
	 * @param edicao the edicao to set
	 */
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	/**
	 * @return the preco
	 */
	public String getPreco() {
		return preco;
	}
	/**
	 * @param preco the preco to set
	 */
	public void setPreco(String preco) {
		this.preco = preco;
	}
	/**
	 * @return the quantidade
	 */
	public String getQuantidade() {
		return quantidade;
	}
	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}
	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}
	
	
}
