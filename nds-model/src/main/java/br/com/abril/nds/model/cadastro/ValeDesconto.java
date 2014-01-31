package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

/**
 * Vale desconto será mapeado como um "produto", conforme
 * solicitado na discussão do conceito de vale desconto na 
 * funcionalidade EMS 089 - Balanceamento de matriz de recolhimento
 *
 * @author francisco.garcia
 *
 */
@Entity
@DiscriminatorValue(value = "VALE_DESCONTO")
public class ValeDesconto extends ProdutoEdicao {

	private static final long serialVersionUID = 1L;
	
	@Column(name="HISTORICO")
	private String historico;
	
	@Column(name="VINCULAR_RECOLHIMENTO")
	private boolean vincularRecolhimentoProdutosCuponados;
	
	/**
	 * Produtos aos quais o vale desconto pode ser aplicado
	 */
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "VALE_DESCONTO_PRODUTO", joinColumns = {@JoinColumn(name = "VALE_DESCONTO_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "PRODUTO_EDICAO_ID")})
	private Set<ProdutoEdicao> produtosAplicacao = new HashSet<ProdutoEdicao>();

	/**
	 * @return the produtosAplicacao
	 */
	public Set<ProdutoEdicao> getProdutosAplicacao() {
		return produtosAplicacao;
	}

	/**
	 * @param produtosAplicacao the produtosAplicacao to set
	 */
	public void setProdutosAplicacao(Set<ProdutoEdicao> produtosAplicacao) {
		this.produtosAplicacao = produtosAplicacao;
	}
	
	/**
	 * Operação não permitida, Vale desconto não permite associação de 
	 * {@link ValeDesconto}
	 * 
	 * @param permiteValeDesconto flag indicando se o {@link ValeDesconto} 
	 * permite vale desconto
	 * 
	 * @throws UnsupportedOperationException caso seja chamado
	 */
	@Override
	public void setPermiteValeDesconto(boolean permiteValeDesconto) {
		throw new UnsupportedOperationException("Vale desconto não permite vale desconto!");
	}
	
	/**
	 * Operação não permitida, {@link ValeDesconto} não possui brinde
	 * 
	 * @param possuiBrinde flag indicando se o {@link ValeDesconto} possui 
	 * brinde
	 * 
	 * @throws UnsupportedOperationException caso seja chamado
	 */
	@Override
	public void setPossuiBrinde(boolean possuiBrinde) {
		throw new UnsupportedOperationException("Vale desconto não possui brinde!");
	}
	
	/**
	 * Operação não permitida, {@link ValeDesconto} não possui expectativa de venda
	 * 
	 * @param expectativaVenda porcentagem da expectativa de venda do {@link ValeDesconto}
	 * 
	 * @throws UnsupportedOperationException caso seja chamado
	 */
	@Override
	public void setExpectativaVenda(BigDecimal expectativaVenda) {
		throw new UnsupportedOperationException("Vale desconto não possui expectativa de venda!");
	}
	
	/**
	 * Define o produto associado ao {@link ValeDesconto}
	 * @param produto Produto para associação ao {@link ValeDesconto}
	 * @throws IllegalArgumentException se o grupo do tipo
	 * do produto não for do grupo {@link GrupoProduto#VALE_DESCONTO}
	 * 
	 */
	@Override
	public void setProduto(Produto produto) {
		TipoProduto tipo = produto.getTipoProduto();
		GrupoProduto grupo = tipo.getGrupoProduto();
		if (!GrupoProduto.VALE_DESCONTO.equals(grupo)) {
			throw new IllegalArgumentException("Produto não é do grupo: " + GrupoProduto.VALE_DESCONTO);
		}
		super.setProduto(produto);
	}

	/**
	 * @return the historico
	 */
	public String getHistorico() {
		return historico;
	}

	/**
	 * @param historico the historico to set
	 */
	public void setHistorico(String historico) {
		this.historico = historico;
	}

	/**
	 * @return the vincularRecolhimentoProdutosCuponados
	 */
	public boolean isVincularRecolhimentoProdutosCuponados() {
		return vincularRecolhimentoProdutosCuponados;
	}

	/**
	 * @param vincularRecolhimentoProdutosCuponados the vincularRecolhimentoProdutosCuponados to set
	 */
	public void setVincularRecolhimentoProdutosCuponados(
			boolean vincularRecolhimentoProdutosCuponados) {
		this.vincularRecolhimentoProdutosCuponados = vincularRecolhimentoProdutosCuponados;
	}
}
