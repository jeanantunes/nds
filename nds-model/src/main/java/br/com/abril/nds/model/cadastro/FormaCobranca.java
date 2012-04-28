package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FORMA_COBRANCA")
@SequenceGenerator(name="FORMA_COBRANCA_SEQ", initialValue = 1, allocationSize = 1)
@DiscriminatorColumn(name = "TIPO_FORMA_COBRANCA", discriminatorType = DiscriminatorType.STRING)
public class FormaCobranca implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2405188133540434339L;

	@Id
	@GeneratedValue(generator = "FORMA_COBRANCA_SEQ")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COBRANCA", nullable = false)
	private TipoCobranca tipoCobranca;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;
	
	@Column(name = "VALOR_MINIMO_EMISSAO")
	private BigDecimal valorMinimoEmissao;
	
	@Column(name = "TAXA_MULTA")
	private BigDecimal taxaMulta;
	
	@Column(name = "TAXA_JUROS_MENSAL")
	private BigDecimal taxaJurosMensal;
	
	@Column(name = "VENCIMENTO_DIA_UTIL", nullable = false)
	private boolean vencimentoDiaUtil;
	
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;
	
	@Column(name = "INSTRUCOES")
	private String instrucoes;
	
	@Column(name = "ATIVA", nullable = false)
	private boolean ativa;
	
	
	//ATRIBUTOS TRANSFERIDOS DE PARAMETRO COBRANCA COTA
	@Column(name = "RECEBE_COBRANCA_EMAIL")
	private boolean recebeCobrancaEmail;
	
	@Embedded
	private ContaBancaria contaBancariaCota;
	
	@OneToMany
	@JoinTable(name = "PARAM_COBRANCA_COTA_FORNECEDOR", joinColumns = {@JoinColumn(name = "PARAM_COBRANCA_COTA_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "FORNECEDOR_ID")})
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
	//---
	
	
	@ManyToOne
	@JoinColumn(name = "PARAMETRO_COBRANCA_COTA_ID")
	private ParametroCobrancaCota parametroCobrancaCota;
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	
	public Banco getBanco() {
		return banco;
	}
	
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
	public BigDecimal getValorMinimoEmissao() {
		return valorMinimoEmissao;
	}
	
	public void setValorMinimoEmissao(BigDecimal valorMinimoEmissao) {
		this.valorMinimoEmissao = valorMinimoEmissao;
	}
	
	public BigDecimal getTaxaMulta() {
		return taxaMulta;
	}
	
	public void setTaxaMulta(BigDecimal taxaMulta) {
		this.taxaMulta = taxaMulta;
	}
	
	public BigDecimal getTaxaJurosMensal() {
		return taxaJurosMensal;
	}

	public void setTaxaJurosMensal(BigDecimal taxaJurosMensal) {
		this.taxaJurosMensal = taxaJurosMensal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isVencimentoDiaUtil() {
		return vencimentoDiaUtil;
	}
	
	public void setVencimentoDiaUtil(boolean vencimentoDiaUtil) {
		this.vencimentoDiaUtil = vencimentoDiaUtil;
	}
	
	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
	public String getInstrucoes() {
		return instrucoes;
	}
	
	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
	}
	
	public boolean isAtiva() {
		return ativa;
	}
	
	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	

	public boolean isRecebeCobrancaEmail() {
		return recebeCobrancaEmail;
	}

	public void setRecebeCobrancaEmail(boolean recebeCobrancaEmail) {
		this.recebeCobrancaEmail = recebeCobrancaEmail;
	}
	
	public ContaBancaria getContaBancariaCota() {
		return contaBancariaCota;
	}
	
	public void setContaBancariaCota(ContaBancaria contaBancariaCota) {
		this.contaBancariaCota = contaBancariaCota;
	}
	
	public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}
	
	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public ParametroCobrancaCota getParametroCobrancaCota() {
		return parametroCobrancaCota;
	}

	
	
	public void setParametroCobrancaCota(ParametroCobrancaCota parametroCobrancaCota) {
		this.parametroCobrancaCota = parametroCobrancaCota;
	}
	
	
}
