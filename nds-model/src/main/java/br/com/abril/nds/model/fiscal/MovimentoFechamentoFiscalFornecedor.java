package br.com.abril.nds.model.fiscal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL_FORNECEDOR")
@DiscriminatorValue(value="FORNECEDOR")
public class MovimentoFechamentoFiscalFornecedor extends MovimentoFechamentoFiscal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@OneToOne
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
}