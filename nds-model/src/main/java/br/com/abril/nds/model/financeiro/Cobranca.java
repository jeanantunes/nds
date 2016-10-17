package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.util.TipoBaixaCobranca;

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
	
	@Column(name = "DIGITO_NOSSO_NUMERO", nullable = true)
	protected String digitoNossoNumero;
	
	@Column(name = "NOSSO_NUMERO_COMPLETO", nullable = true, unique = true)
	protected String nossoNumeroCompleto;
	
	@Column(name = "NOSSO_NUMERO_CONSOLIDADO", nullable = true)
	protected String nossoNumeroConsolidado;
	
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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_BAIXA", nullable = true)
	protected TipoBaixaCobranca tipoBaixa;
	
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
	
	@OneToMany(mappedBy = "cobranca", cascade = CascadeType.ALL)
	protected List<BaixaCobranca> baixasCobranca = new ArrayList<BaixaCobranca>();
	
	@Column(name="VIAS")
	protected Integer vias;

	@ManyToOne
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;
	
	@Column(name = "ORIUNDA_NEGOCIACAO_AVULSA")
	private boolean oriundaNegociacaoAvulsa;
	
	@ManyToOne
	@JoinColumn(name="FORNECEDOR_ID")
	private Fornecedor fornecedor;
    
	@ManyToMany(mappedBy="cobrancasOriginarias")
	private List<Negociacao> negociacao;
	
	@Column(name="ENVIAR_POR_EMAIL")
	private boolean enviarPorEmail;
	
	@ManyToOne
	@JoinColumn(name="COBRANCA_CENTRALIZACAO_ID")
	private CobrancaCentralizacao cobrancaCentralizacao;
	
	@Column(name="OBSERVACAO")
	private String observacao;
	
	@Column(name="COBRANCA_NFE")
	private boolean cobrancaNFe;
	
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

	/**
	 * @return the digitoNossoNumero
	 */
	public String getDigitoNossoNumero() {
		return digitoNossoNumero;
	}

	/**
	 * @param digitoNossoNumero the digitoNossoNumero to set
	 */
	public void setDigitoNossoNumero(String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}

	/**
	 * @return the nossoNumeroCompleto
	 */
	public String getNossoNumeroCompleto() {
		return nossoNumeroCompleto;
	}

	/**
	 * @param nossoNumeroCompleto the nossoNumeroCompleto to set
	 */
	public void setNossoNumeroCompleto(String nossoNumeroCompleto) {
		this.nossoNumeroCompleto = nossoNumeroCompleto;
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

	public TipoBaixaCobranca getTipoBaixa() {
		return tipoBaixa;
	}

	public void setTipoBaixa(TipoBaixaCobranca tipoBaixa) {
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
	
	public List<BaixaCobranca> getBaixasCobranca() {
		return baixasCobranca;
	}
	
	public void setBaixasCobranca(List<BaixaCobranca> baixasCobranca) {
		this.baixasCobranca = baixasCobranca;
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

	public boolean isOriundaNegociacaoAvulsa() {
		return oriundaNegociacaoAvulsa;
	}

	public void setOriundaNegociacaoAvulsa(boolean oriundaNegociacaoAvulsa) {
		this.oriundaNegociacaoAvulsa = oriundaNegociacaoAvulsa;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the negociacao
	 */
	public List<Negociacao> getNegociacao() {
		return negociacao;
	}

	/**
	 * @param negociacao the negociacao to set
	 */
	public void setNegociacao(List<Negociacao> negociacao) {
		this.negociacao = negociacao;
	}

	/**
	 * @return the enviarPorEmail
	 */
	public boolean isEnviarPorEmail() {
		return enviarPorEmail;
	}

	/**
	 * @param enviarPorEmail the enviarPorEmail to set
	 */
	public void setEnviarPorEmail(boolean enviarPorEmail) {
		this.enviarPorEmail = enviarPorEmail;
	}

	/**
	 * @return the cobrancaCentralizacao
	 */
	public CobrancaCentralizacao getCobrancaCentralizacao() {
		return cobrancaCentralizacao;
	}

	/**
	 * @param cobrancaCentralizacao the cobrancaCentralizacao to set
	 */
	public void setCobrancaCentralizacao(CobrancaCentralizacao cobrancaCentralizacao) {
		this.cobrancaCentralizacao = cobrancaCentralizacao;
	}

	public String getNossoNumeroConsolidado() {
		return nossoNumeroConsolidado;
	}

	public void setNossoNumeroConsolidado(String nossoNumeroConsolidado) {
		this.nossoNumeroConsolidado = nossoNumeroConsolidado;
	}
	
	public boolean isCobrancaNFe() {
		return cobrancaNFe;
	}

	public void setCobrancaNFe(boolean cobrancaNFe) {
		this.cobrancaNFe = cobrancaNFe;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}