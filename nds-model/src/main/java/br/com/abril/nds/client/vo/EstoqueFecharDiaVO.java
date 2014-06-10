package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class EstoqueFecharDiaVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long produto;
	
	private BigInteger exemplar;
	
	private BigDecimal venda;

	public Long getProduto() {
		return produto;
	}

	public void setProduto(Long produto) {
		this.produto = produto;
	}

	public BigInteger getExemplar() {
		return exemplar;
	}

	public void setExemplar(BigInteger exemplar) {
		this.exemplar = exemplar;
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}

	
}
