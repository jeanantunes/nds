package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "COBRANCA_CENTRALIZACAO")
@SequenceGenerator(name="COBRANCA_CENTRALIZACAO_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CobrancaCentralizacao {
	
	@Id
	@GeneratedValue(generator = "COBRANCA_CENTRALIZACAO_SEQ")
	@Column(name = "ID")
	protected Long id;
	
	@Column(name = "NOSSO_NUMERO", nullable = false, unique = true)
	protected String nossoNumero;
	
	@Column(name = "DIGITO_NOSSO_NUMERO", nullable = true)
	protected String digitoNossoNumero;
	
	@Column(name = "NOSSO_NUMERO_COMPLETO", nullable = true, unique = true)
	protected String nossoNumeroCompleto;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_EMISSAO", nullable = false)
	protected Date dataEmissao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_VENCIMENTO", nullable = false)
	protected Date dataVencimento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PAGAMENTO", nullable = true)
	protected Date dataPagamento;
	
	@Column(name = "ENCARGOS", nullable = true, precision=18, scale=4)
	protected BigDecimal encargos;

	@Column(name = "VALOR", nullable = false, precision=18, scale=4)
	protected BigDecimal valor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_BAIXA", nullable = true)
	protected TipoBaixaCobranca tipoBaixa;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COBRANCA", nullable = false)
	protected TipoCobranca tipoCobranca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_COBRANCA", nullable = false)
	protected StatusCobranca statusCobranca;
	
	@Column(name="VIAS")
	protected Integer vias;
	
	@Column(name = "CONTEMPLACAO", nullable = true)
	protected boolean contemplacao;
	
	@Column(name="ENVIAR_POR_EMAIL")
	private boolean enviarPorEmail;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;
	
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	protected Cota cota;
	
	@ManyToOne
	@JoinColumn(name="FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	@OneToMany(mappedBy = "cobrancaCentralizacao")
	protected List<Cobranca> cobrancasCentralizadas = new ArrayList<Cobranca>();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * @param nossoNumero the nossoNumero to set
	 */
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

	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @param dataPagamento the dataPagamento to set
	 */
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the encargos
	 */
	public BigDecimal getEncargos() {
		return encargos;
	}

	/**
	 * @param encargos the encargos to set
	 */
	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the tipoBaixa
	 */
	public TipoBaixaCobranca getTipoBaixa() {
		return tipoBaixa;
	}

	/**
	 * @param tipoBaixa the tipoBaixa to set
	 */
	public void setTipoBaixa(TipoBaixaCobranca tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}

	/**
	 * @return the tipoCobranca
	 */
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	/**
	 * @param tipoCobranca the tipoCobranca to set
	 */
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	/**
	 * @return the statusCobranca
	 */
	public StatusCobranca getStatusCobranca() {
		return statusCobranca;
	}

	/**
	 * @param statusCobranca the statusCobranca to set
	 */
	public void setStatusCobranca(StatusCobranca statusCobranca) {
		this.statusCobranca = statusCobranca;
	}

	/**
	 * @return the vias
	 */
	public Integer getVias() {
		return vias;
	}

	/**
	 * @param vias the vias to set
	 */
	public void setVias(Integer vias) {
		this.vias = vias;
	}

	/**
	 * @return the contemplacao
	 */
	public boolean isContemplacao() {
		return contemplacao;
	}

	/**
	 * @param contemplacao the contemplacao to set
	 */
	public void setContemplacao(boolean contemplacao) {
		this.contemplacao = contemplacao;
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
	 * @return the banco
	 */
	public Banco getBanco() {
		return banco;
	}

	/**
	 * @param banco the banco to set
	 */
	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	/**
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}

	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * @return the fornecedor
	 */
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the cobrancasCentralizadas
	 */
	public List<Cobranca> getCobrancasCentralizadas() {
		return cobrancasCentralizadas;
	}

	/**
	 * @param cobrancasCentralizadas the cobrancasCentralizadas to set
	 */
	public void setCobrancasCentralizadas(List<Cobranca> cobrancasCentralizadas) {
		this.cobrancasCentralizadas = cobrancasCentralizadas;
	}
}