package br.com.abril.nds.model.movimentacao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;

@MappedSuperclass
public abstract class AbstractMovimentoEstoque extends Movimento {

	@Column(name = "QTDE", nullable = false)
	protected BigDecimal qtde;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	protected ProdutoEdicao produtoEdicao;
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_MOVIMENTO_ID")
	protected TipoMovimentoEstoque tipoMovimento;
	
	public BigDecimal getQtde() {
		return qtde;
	}
	
	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public TipoMovimentoEstoque getTipoMovimento() {
		return tipoMovimento;
	}
	
	public void setTipoMovimento(TipoMovimentoEstoque tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	

}
