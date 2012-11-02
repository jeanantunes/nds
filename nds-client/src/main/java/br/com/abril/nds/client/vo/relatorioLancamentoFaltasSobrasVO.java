package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.util.CurrencyUtil;

public class relatorioLancamentoFaltasSobrasVO implements Serializable  {

	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5342968572475646951L;
	
	
	private String 	codigoProduto;
	private String 	descricaoProduto;
	private String 	numeroEdicao;
	private String 	precoCapa;
	private String 	reparte;
	
	public relatorioLancamentoFaltasSobrasVO(Diferenca diferenca) {
		super();
		ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
		Produto produto = produtoEdicao.getProduto();
		
		this.codigoProduto = produto.getCodigo();
		this.descricaoProduto = produto.getNomeComercial();;
		this.numeroEdicao = produtoEdicao.getNumeroEdicao().toString();
		this.precoCapa = CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda());
		this.reparte = diferenca.getQtde().toString();
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}

	public String getReparte() {
		return reparte;
	}

	public void setReparte(String reparte) {
		this.reparte = reparte;
	}
	
	

	
	
		
}
