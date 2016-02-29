package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@Table(name="FECHAMENTO_DIARIO_DIVIDA")
@SequenceGenerator(name="FECHAMENTO_DIARIO_DIVIDA_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioDivida implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_DIVIDA_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID")
	private FechamentoDiarioConsolidadoDivida fechamentoDiarioConsolidadoDivida;
	
	@Column(name="NUMERO_COTA")
	private Integer numeroCota;
	
	@Column(name="NOME_COTA")
	private String nomeCota;
	
	@Column(name="BANCO")
	private String banco;
	
	@Column(name="NUMERO_CONTA")
	private String numeroConta;
	
	@Column(name="NOSSO_NUMERO")
	private String nossoNumero;
	
	@Column(name="VALOR")
	private BigDecimal valor;
	
	@Column(name="DATA_EMISSAO")
	@Temporal(TemporalType.DATE)
	private Date dataEmissao;
	
	@Column(name="DATA_VENCIMENTO")
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@Column(name="FORMA_PAGAMENTO")
	@Enumerated(EnumType.STRING)
	private TipoCobranca tipoCobranca;
	
	@Column(name="ID_DIVIDA")
	private Long idntificadorDivida;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FechamentoDiarioConsolidadoDivida getFechamentoDiarioConsolidadoDivida() {
		return fechamentoDiarioConsolidadoDivida;
	}

	public void setFechamentoDiarioConsolidadoDivida(
			FechamentoDiarioConsolidadoDivida fechamentoDiarioConsolidadoDivida) {
		this.fechamentoDiarioConsolidadoDivida = fechamentoDiarioConsolidadoDivida;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public Long getIdntificadorDivida() {
		return idntificadorDivida;
	}

	public void setIdntificadorDivida(Long idntificadorDivida) {
		this.idntificadorDivida = idntificadorDivida;
	}
	
	
	
	
}
