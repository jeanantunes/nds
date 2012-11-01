package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "HISTORICO_FECHAMENTO_DIARIO_LANCAMENTO_ENCALHE")
@SequenceGenerator(name = "HISTORICO_FECHAMENTO_DIARIO_LANCAMENTO_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoFechamentoDiarioLancamentoEncalhe implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "HISTORICO_FECHAMENTO_DIARIO_LANCAMENTO_ENCALHE_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID")
	private HistoricoFechamentoDiarioConsolidadoEncalhe historicoConsolidadoEncalhe;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QUANTIDADE")
	private BigInteger quantidade;
	
	@Column(name="QNT_VENDA_ENCALHE")
	private BigInteger quantidadeVendaEncalhe;
	
	@Column(name="QNT_DIFERENCA")
	private BigInteger quantidadeDiferenca;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public BigInteger getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}

	public BigInteger getQuantidadeVendaEncalhe() {
		return quantidadeVendaEncalhe;
	}

	public void setQuantidadeVendaEncalhe(BigInteger quantidadeVendaEncalhe) {
		this.quantidadeVendaEncalhe = quantidadeVendaEncalhe;
	}

	public BigInteger getQuantidadeDiferenca() {
		return quantidadeDiferenca;
	}

	public void setQuantidadeDiferenca(BigInteger quantidadeDiferenca) {
		this.quantidadeDiferenca = quantidadeDiferenca;
	}

	public HistoricoFechamentoDiarioConsolidadoEncalhe getHistoricoConsolidadoEncalhe() {
		return historicoConsolidadoEncalhe;
	}

	public void setHistoricoConsolidadoEncalhe(
			HistoricoFechamentoDiarioConsolidadoEncalhe historicoConsolidadoEncalhe) {
		this.historicoConsolidadoEncalhe = historicoConsolidadoEncalhe;
	}
	
	
}
