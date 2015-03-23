package br.com.abril.nds.model.movimentacao;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@MappedSuperclass
public abstract class AbstractMovimentoEstoque extends Movimento {

	@Column(name = "QTDE", nullable = false)
	protected BigInteger qtde;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	protected ProdutoEdicao produtoEdicao;
	
	@Column(name="ORIGEM", columnDefinition="VARCHAR(50) default 'MANUAL'", updatable = true)
	@Enumerated(EnumType.STRING)
	private Origem origem;
	
	public BigInteger getQtde() {
		return qtde;
	}
	
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
}
