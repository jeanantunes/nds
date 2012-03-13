package br.com.abril.nds.model.estoque;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;

@Entity
@Table(name = "MOVIMENTO_ESTOQUE")
public class MovimentoEstoque extends AbstractMovimentoEstoque {

	@OneToOne(optional = true)
	@JoinColumn(name = "ITEM_REC_FISICO_ID")
	private ItemRecebimentoFisico itemRecebimentoFisico;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ESTOQUE_PRODUTO_ID")
	private EstoqueProduto estoqueProduto;

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
	
	public ItemRecebimentoFisico getItemRecebimentoFisico() {
		return itemRecebimentoFisico;
	}
	
	public void setItemRecebimentoFisico(ItemRecebimentoFisico itemRecebimentoFisico) {
		this.itemRecebimentoFisico = itemRecebimentoFisico;
	}
	
	public EstoqueProduto getEstoqueProduto() {
		return estoqueProduto;
	}
	
	public void setEstoqueProduto(EstoqueProduto estoqueProduto) {
		this.estoqueProduto = estoqueProduto;
	}
	
	public TipoMovimentoEstoque getTipoMovimento() {
		return tipoMovimento;
	}
	
	public void setTipoMovimento(TipoMovimentoEstoque tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

}