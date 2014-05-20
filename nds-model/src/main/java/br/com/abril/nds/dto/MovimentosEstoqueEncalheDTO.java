package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class MovimentosEstoqueEncalheDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5641931955549914671L;
	
	private Long idMovimentoEstoqueCota;
	private Long idCota;
	private Long idFornecedor;
	private Long idProdutoEdicao;
	
	private BigInteger qtde;
	
	private BigDecimal precoVenda;
	
	private BigDecimal precoComDesconto;
	
	private BigDecimal valorDesconto;

	public Long getIdMovimentoEstoqueCota() {
		return idMovimentoEstoqueCota;
	}

	public void setIdMovimentoEstoqueCota(Long idMovimentoEstoqueCota) {
		this.idMovimentoEstoqueCota = idMovimentoEstoqueCota;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

}
