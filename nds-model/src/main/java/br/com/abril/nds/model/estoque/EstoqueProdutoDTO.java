package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigInteger;

public class EstoqueProdutoDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long produtoEdicaoId;
	
	private BigInteger qtde;
	
	private BigInteger qtdeSuplementar;
	
	private BigInteger qtdeDevolucaoEncalhe;
	
	private BigInteger qtdeDanificado;
	
	private BigInteger qtdeJuramentado;
	
	
	public EstoqueProdutoDTO() {
	}
	
	public EstoqueProdutoDTO(Long id) {
		this.id=id;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getProdutoEdicaoId() {
		return produtoEdicaoId;
	}

	public void setProdutoEdicaoId(Long produtoEdicaoId) {
		this.produtoEdicaoId = produtoEdicaoId;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public BigInteger getQtdeSuplementar() {
		return qtdeSuplementar;
	}

	public void setQtdeSuplementar(BigInteger qtdeSuplementar) {
		this.qtdeSuplementar = qtdeSuplementar;
	}

	public BigInteger getQtdeDevolucaoEncalhe() {
		return qtdeDevolucaoEncalhe;
	}

	public void setQtdeDevolucaoEncalhe(BigInteger qtdeDevolucaoEncalhe) {
		this.qtdeDevolucaoEncalhe = qtdeDevolucaoEncalhe;
	}

	public BigInteger getQtdeDanificado() {
		return qtdeDanificado;
	}

	public void setQtdeDanificado(BigInteger qtdeDanificado) {
		this.qtdeDanificado = qtdeDanificado;
	}

	public BigInteger getQtdeJuramentado() {
		return qtdeJuramentado;
	}

	public void setQtdeJuramentado(BigInteger qtdeJuramentado) {
		this.qtdeJuramentado = qtdeJuramentado;
	}
	
}
