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
@Table(name = "FECHAMENTO_DIARIO_LANCAMENTO_ENCALHE")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_LANCAMENTO_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioLancamentoEncalhe implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_LANCAMENTO_ENCALHE_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID")
	private FechamentoDiarioConsolidadoEncalhe fechamentoDiarioConsolidadoEncalhe;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PRODUTO_EDICAO")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name="QUANTIDADE")
	private BigInteger quantidade;
	
	@Column(name="QNT_VENDA_ENCALHE")
	private BigInteger quantidadeVendaEncalhe;
	
	@Column(name="QNT_DIFERENCA")
	private BigInteger quantidadeDiferenca;
	
	@Column(name="QNT_LOGICO_JURAMENTADO")
	private BigInteger quantidadeLogicoJuramentado;
	
	@Column(name="QNT_FISICO")
	private BigInteger quantidadeFisico;
	
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

	public FechamentoDiarioConsolidadoEncalhe getFechamentoDiarioConsolidadoEncalhe() {
		return fechamentoDiarioConsolidadoEncalhe;
	}

	public void setFechamentoDiarioConsolidadoEncalhe(
			FechamentoDiarioConsolidadoEncalhe fechamentoDiarioConsolidadoEncalhe) {
		this.fechamentoDiarioConsolidadoEncalhe = fechamentoDiarioConsolidadoEncalhe;
	}

	public BigInteger getQuantidadeLogicoJuramentado() {
		return quantidadeLogicoJuramentado;
	}

	public void setQuantidadeLogicoJuramentado(
			BigInteger quantidadeLogicoJuramentado) {
		this.quantidadeLogicoJuramentado = quantidadeLogicoJuramentado;
	}

	public BigInteger getQuantidadeFisico() {
		return quantidadeFisico;
	}

	public void setQuantidadeFisico(BigInteger quantidadeFisico) {
		this.quantidadeFisico = quantidadeFisico;
	}
	
	
	
	
}