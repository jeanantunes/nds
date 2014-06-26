package br.com.abril.nds.model.fiscal;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.movimentacao.TipoMovimento;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name="TIPO_DESTINATARIO", discriminatorType=DiscriminatorType.STRING)
public abstract class MovimentoFechamentoFiscal implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6222933915307416834L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Date data;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", nullable = false)
	private Date dataCriacao;
	
	@Column(name = "NOTA_FISCAL_DEVOLUCAO_SIMBOLICA_EMITIDA")
	private boolean notaFiscalDevolucaoSimbolicaEmitida;
	
	@Column(name = "DESOBRIGA_NOTA_FISCAL_DEVOLUCAO_SIMBOLICA")
	private boolean desobrigaNotaFiscalDevolucaoSimbolica;
	
	@Column(name = "NOTA_FISCAL_LIBERADA_EMISSAO")
	private boolean notaFiscalLiberadaEmissao;
	
	@OneToOne
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name = "QTDE")
	private BigInteger qtde;
	
	@Embedded
	private ValoresAplicados valoresAplicados;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_DESTINATARIO")
	private TipoDestinatario tipoDestinatario;
	
	@OneToOne
	@JoinColumn(name="TIPO_MOVIMENTO_ID")
	private TipoMovimento tipoMovimento;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="movimentoFechamentoFiscal")
	private List<OrigemItemMovFechamentoFiscal> origemMovimentoFechamentoFiscal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public boolean isDesobrigaNotaFiscalDevolucaoSimbolica() {
		return desobrigaNotaFiscalDevolucaoSimbolica;
	}

	public void setDesobrigaNotaFiscalDevolucaoSimbolica(
			boolean desobrigaNotaFiscalDevolucaoSimbolica) {
		this.desobrigaNotaFiscalDevolucaoSimbolica = desobrigaNotaFiscalDevolucaoSimbolica;
	}

	public boolean isNotaFiscalLiberadaEmissao() {
		return notaFiscalLiberadaEmissao;
	}

	public void setNotaFiscalLiberadaEmissao(boolean notaFiscalLiberadaEmissao) {
		this.notaFiscalLiberadaEmissao = notaFiscalLiberadaEmissao;
	}

	public boolean isNotaFiscalDevolucaoSimbolicaEmitida() {
		return notaFiscalDevolucaoSimbolicaEmitida;
	}

	public void setNotaFiscalDevolucaoSimbolicaEmitida(
			boolean notaFiscalDevolucaoSimbolicaEmitida) {
		this.notaFiscalDevolucaoSimbolicaEmitida = notaFiscalDevolucaoSimbolicaEmitida;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public ValoresAplicados getValoresAplicados() {
		return valoresAplicados;
	}

	public void setValoresAplicados(ValoresAplicados valoresAplicados) {
		this.valoresAplicados = valoresAplicados;
	}
	
	public TipoDestinatario getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public TipoMovimento getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public List<OrigemItemMovFechamentoFiscal> getOrigemMovimentoFechamentoFiscal() {
		return origemMovimentoFechamentoFiscal;
	}

	public void setOrigemMovimentoFechamentoFiscal(
			List<OrigemItemMovFechamentoFiscal> origemMovimentoFechamentoFiscal) {
		this.origemMovimentoFechamentoFiscal = origemMovimentoFechamentoFiscal;
	}
	
}