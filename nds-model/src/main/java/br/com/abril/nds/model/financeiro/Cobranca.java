package br.com.abril.nds.model.financeiro;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Cota;

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
	private Long id;
	
	@Column(name = "NOSSO_NUMERO", nullable = false, unique = true)
	private String nossoNumero;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_EMISSAO", nullable = false)
	private Date dataEmissao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_VENCIMENTO", nullable = false)
	private Date dataVencimento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PAGAMENTO", nullable = true)
	private Date dataPagamento;
	
	@Column(name = "ENCARGOS", nullable = true)
	private String encargos;

	@Column(name = "VALOR", nullable = false)
	private Double valor;
	
	@Column(name = "TIPO_BAIXA", nullable = true)
	private String tipoBaixa;
	
	@Column(name = "ACAO", nullable = true)
	private String acao;
	
	@Column(name = "CONTEMPLACAO", nullable = true)
	private boolean contemplacao;
	
	@ManyToOne(optional = false)
	private Cota cota;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_COBRANCA", nullable = false)
	private StatusCobranca statusCobranca;
    
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

	public String getEncargos() {
		return encargos;
	}

	public void setEncargos(String encargos) {
		this.encargos = encargos;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getTipoBaixa() {
		return tipoBaixa;
	}

	public void setTipoBaixa(String tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
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
	
}