package br.com.abril.nds.model.movimentacao;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@MappedSuperclass
public abstract class AbstractMovimentoEstoque extends Movimento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8276793750364718688L;

	@Column(name = "QTDE", nullable = false)
	protected BigInteger qtde;
	
	@ManyToOne(optional = false)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	protected ProdutoEdicao produtoEdicao;
	
	@Column(name = "NOTA_FISCAL_EMITIDA")
	private boolean notaFiscalEmitida;
	
	@Column(name="ORIGEM",columnDefinition="VARCHAR(50) default 'MANUAL'", insertable = false, updatable = true)
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

	public boolean isNotaFiscalEmitida() {
		return notaFiscalEmitida;
	}

	public void setNotaFiscalEmitida(boolean notaFiscalEmitida) {
		this.notaFiscalEmitida = notaFiscalEmitida;
	}
	
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
}
