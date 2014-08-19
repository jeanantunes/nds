package br.com.abril.nds.dto;

import java.math.BigInteger;

import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;

public class EstoqueProdutoFilaDTO {

	private Long id;
	private Long idProdutoEdicao;
	private Long idCota;
	private BigInteger qtde;
	private TipoEstoque tipoEstoque;
	private OperacaoEstoque operacaoEstoque;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	public BigInteger getQtde() {
		return qtde;
	}
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}
	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}
	public OperacaoEstoque getOperacaoEstoque() {
		return operacaoEstoque;
	}
	public void setOperacaoEstoque(OperacaoEstoque operacaoEstoque) {
		this.operacaoEstoque = operacaoEstoque;
	}
	
	
}
