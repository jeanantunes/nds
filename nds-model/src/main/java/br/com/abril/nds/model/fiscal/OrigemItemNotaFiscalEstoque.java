package br.com.abril.nds.model.fiscal;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public class OrigemItemNotaFiscalEstoque extends OrigemItemNotaFiscal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6208480018096488241L;
	
	OrigemItem origem = OrigemItem.ESTOQUE;
	
	private ProdutoEdicao produtoEdicao;	

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public OrigemItem getOrigem() {
		return origem;
	}

	public OrigemItemNotaFiscal obterOrigemItemNotaFiscal() {
		return this;
	};
	
}