package br.com.abril.nds.model.fiscal.nota;

import java.math.BigDecimal;

/**
 * Item da Nota
 * @author Diego Fernandes
 */
public class ItemNotaFiscal {
	
	private long idProdutoEdicao;
	
	private BigDecimal quantidade;
	
	private BigDecimal valorUnitario;
	
	private String cstICMS;
	
	public ItemNotaFiscal() {
	}

	public ItemNotaFiscal(long idProdutoEdicao, BigDecimal quantidade,
			BigDecimal valorUnitario, String cstICMS) {
		super();
		this.idProdutoEdicao = idProdutoEdicao;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.cstICMS = cstICMS;
	}




	/**
	 * @return the idProdutoEdicao
	 */
	public long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * @return the quantidade
	 */
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the cstICMS
	 */
	public String getCstICMS() {
		return cstICMS;
	}

	/**
	 * @param cstICMS the cstICMS to set
	 */
	public void setCstICMS(String cstICMS) {
		this.cstICMS = cstICMS;
	}
	
	

}
