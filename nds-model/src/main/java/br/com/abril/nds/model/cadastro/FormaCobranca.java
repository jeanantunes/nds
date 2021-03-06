package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FORMA_COBRANCA")
@SequenceGenerator(name="FORMA_COBRANCA_SEQ", initialValue = 1, allocationSize = 1)
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
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "FORMA_COBRANCA_BOLETO", nullable = true)
	private FormaCobrancaBoleto formaCobrancaBoleto;
	
	@Column(name = "PROTESTAR_BOLETO_REGISTRADO", nullable = false)
	private boolean protestarBoletoRegistrado;

	@Column(name = "QTD_DIAS_PROTESTO", nullable = true)
	private Long quantidadeDiasParaProtesto;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_FORMA_COBRANCA", nullable = false)
	private TipoFormaCobranca tipoFormaCobranca;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;
	
	@Column(name = "TAXA_MULTA")
	private BigDecimal taxaMulta;
	
	@Column(name = "VALOR_MULTA", precision=18, scale=4)
	private BigDecimal valorMulta;
	
	@Column(name = "TAXA_JUROS_MENSAL", precision=18, scale=4)
	private BigDecimal taxaJurosMensal;
	
	@Column(name = "VENCIMENTO_DIA_UTIL", nullable = false)
	private boolean vencimentoDiaUtil;
	
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;
	
	@Column(name = "INSTRUCOES")
	private String instrucoes;
	
	@Column(name = "ATIVA", nullable = false)
	private boolean ativa;

	@Column(name = "RECEBE_COBRANCA_EMAIL")
	private boolean recebeCobrancaEmail;
	
	@Embedded
	private ContaBancariaDeposito contaBancariaCota;
	
	@OneToOne(mappedBy="formaCobranca")
	private PoliticaCobranca politicaCobranca;
	
	@ManyToMany  
	@JoinTable(name = "FORMA_COBRANCA_FORNECEDOR", joinColumns = {@JoinColumn(name = "FORMA_COBRANCA_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "FORNECEDOR_ID")})
	private Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
	
	@ManyToOne(fetch = FetchType.EAGER)  
	@JoinColumn(name = "PARAMETRO_COBRANCA_COTA_ID")
	private ParametroCobrancaCota parametroCobrancaCota;

	@ElementCollection(fetch = FetchType.EAGER)  
	@OrderBy
	private List<Integer> diasDoMes; 
	
	@OneToMany(mappedBy="formaCobranca", fetch = FetchType.EAGER)
	@OrderBy("codigoDiaSemana ASC")
	private Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota = new HashSet<ConcentracaoCobrancaCota>();
	
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
	
	public TipoFormaCobranca getTipoFormaCobranca() {
		return tipoFormaCobranca;
	}

	public void setTipoFormaCobranca(TipoFormaCobranca tipoFormaCobranca) {
		this.tipoFormaCobranca = tipoFormaCobranca;
	}

	public Banco getBanco() {
		return banco;
	}
	
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
	public BigDecimal getTaxaMulta() {
		return taxaMulta;
	}
	
	public void setTaxaMulta(BigDecimal taxaMulta) {
		this.taxaMulta = taxaMulta;
	}
	
	/**
	 * @return the valorMulta
	 */
	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	/**
	 * @param valorMulta the valorMulta to set
	 */
	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
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
	
	public ContaBancariaDeposito getContaBancariaCota() {
		return contaBancariaCota;
	}
	
	public void setContaBancariaCota(ContaBancariaDeposito contaBancariaCota) {
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

	public Set<ConcentracaoCobrancaCota> getConcentracaoCobrancaCota() {
		return concentracaoCobrancaCota;
	}
	
	public void setConcentracaoCobrancaCota(
			Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota) {
		this.concentracaoCobrancaCota = concentracaoCobrancaCota;
	}

	/**
	 * @return the diasDoMes
	 */
	public List<Integer> getDiasDoMes() {
		return diasDoMes;
	}

	/**
	 * @param diasDoMes the diasDoMes to set
	 */
	public void setDiasDoMes(List<Integer> diasDoMes) {
		this.diasDoMes = diasDoMes;
	}

	/**
	 * @return the formaCobrancaBoleto
	 */
	public FormaCobrancaBoleto getFormaCobrancaBoleto() {
		return formaCobrancaBoleto;
	}

	/**
	 * @param formaCobrancaBoleto the formaCobrancaBoleto to set
	 */
	public void setFormaCobrancaBoleto(FormaCobrancaBoleto formaCobrancaBoleto) {
		this.formaCobrancaBoleto = formaCobrancaBoleto;
	}

	/**
	 * @return the politicaCobranca
	 */
	public PoliticaCobranca getPoliticaCobranca() {
		return politicaCobranca;
	}

	/**
	 * @param politicaCobranca the politicaCobranca to set
	 */
	public void setPoliticaCobranca(PoliticaCobranca politicaCobranca) {
		this.politicaCobranca = politicaCobranca;
	}
	
	public boolean isProtestarBoletoRegistrado() {
		return protestarBoletoRegistrado;
	}

	public void setProtestarBoletoRegistrado(boolean protestarBoletoRegistrado) {
		this.protestarBoletoRegistrado = protestarBoletoRegistrado;
	}

	public Long getQuantidadeDiasParaProtesto() {
		return quantidadeDiasParaProtesto;
	}

	public void setQuantidadeDiasParaProtesto(Long quantidadeDiasParaProtesto) {
		this.quantidadeDiasParaProtesto = quantidadeDiasParaProtesto;
	}
	
}
