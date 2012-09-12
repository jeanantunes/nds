package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;

/**
 * @author luiz.marcili
 * @version 1.0
 * @created 06-mar-2012 11:07:00
 */
@Entity
@Table(name = "COBRANCA")
@SequenceGenerator(name="COBRANCA_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_DOCUMENTO", discriminatorType = DiscriminatorType.STRING)
public abstract class Cobranca {
	
	@Id
	@GeneratedValue(generator = "COBRANCA_SEQ")
	@Column(name = "ID")
	protected Long id;
	
	@Column(name = "NOSSO_NUMERO", nullable = false, unique = true)
	protected String nossoNumero;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_EMISSAO", nullable = false)
	protected Date dataEmissao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_VENCIMENTO", nullable = false)
	protected Date dataVencimento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PAGAMENTO", nullable = true)
	protected Date dataPagamento;
	
	@Column(name = "ENCARGOS", nullable = true)
	protected BigDecimal encargos;

	@Column(name = "VALOR", nullable = false)
	protected BigDecimal valor;
	
	@Column(name = "TIPO_BAIXA", nullable = true)
	protected String tipoBaixa;
	
	@Column(name = "CONTEMPLACAO", nullable = true)
	protected boolean contemplacao;
	
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	protected Cota cota;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_COBRANCA", nullable = false)
	protected StatusCobranca statusCobranca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COBRANCA", nullable = false)
	protected TipoCobranca tipoCobranca;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "DIVIDA_ID")
	protected Divida divida;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "BAIXA_COBRANCA_ID")
	protected BaixaCobranca baixaCobranca;
	
	@Column(name="VIAS")
	protected Integer vias;

	@ManyToOne
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;
	
	@ManyToOne
	@JoinColumn(name="FORNECEDOR_ID")
	private Fornecedor fornecedor;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getEncargos() {
		return encargos;
	}

	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getTipoBaixa() {
		return tipoBaixa;
	}

	public void setTipoBaixa(String tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}

	public boolean isContemplacao() {
		return contemplacao;
	}

	public void setContemplacao(boolean contemplacao) {
		this.contemplacao = contemplacao;
	}
    
	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public StatusCobranca getStatusCobranca() {
		return statusCobranca;
	}

	public void setStatusCobranca(StatusCobranca statusCobranca) {
		this.statusCobranca = statusCobranca;
	}
	
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	
	public Divida getDivida() {
		return divida;
	}
	
	public void setDivida(Divida divida) {
		this.divida = divida;
	}
	
	public BaixaCobranca getBaixaCobranca() {
		return baixaCobranca;
	}
	
	public void setBaixaCobranca(BaixaCobranca baixaCobranca) {
		this.baixaCobranca = baixaCobranca;
	}

	public Integer getVias() {
		return vias;
	}

	public void setVias(Integer vias) {
		this.vias = vias;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
}