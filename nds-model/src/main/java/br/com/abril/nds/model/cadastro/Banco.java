package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "BANCO",
	uniqueConstraints = { @UniqueConstraint(columnNames = {"NUMERO_BANCO", "AGENCIA", "DV_AGENCIA", "CONTA", "DV_CONTA" }) } )
@SequenceGenerator(name="BANCO_SEQ", initialValue = 1, allocationSize = 1)
public class Banco implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9021552193435936682L;

	@Id
	@GeneratedValue(generator = "BANCO_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "NUMERO_BANCO", nullable = false)
	private String numeroBanco;

	@Column(name = "NOME", nullable = false)
	private String nome;

	@Column(name = "APELIDO", nullable = false)
	private String apelido;

	@Column(name = "AGENCIA", nullable = false)
	private Long agencia;

	@Column(name = "DV_AGENCIA")
	private String dvAgencia;

	@Column(name = "CONTA", nullable = false)
	private Long conta;

	@Column(name = "DV_CONTA")
	private String dvConta;

	@OneToOne
	@JoinColumn(name = "PESSOA_JURIDICA_CEDENTE_ID", nullable = false)
	private Pessoa pessoaJuridicaCedente;

	@Column(name = "CODIGO_CEDENTE", nullable = false)
	private String codigoCedente;

	@Column(name = "DIGITO_CODIGO_CEDENTE")
	private String digitoCodigoCedente;

	@Column(name = "CARTEIRA")
	private Integer carteira;

	@Column(name = "ATIVO", nullable = false)
	private boolean ativo;

	@Column(name = "EXIBIR_VALOR_MONETARIO")
	private boolean exibirValorMonetario;

	@Column(name = "INSTRUCOES_1")
	private String instrucoes1;

	@Column(name = "INSTRUCOES_2")
	private String instrucoes2;

	@Column(name = "INSTRUCOES_3")
	private String instrucoes3;

	@Column(name = "INSTRUCOES_4")
	private String instrucoes4;

	@Column(name = "JUROS")
	private BigDecimal juros;

	@Column(name = "MULTA")
	private BigDecimal multa;

	@Column(name = "VR_MULTA")
	private BigDecimal vrMulta;

	@Column(name = "CODIGO_EMPRESA")
	private String codigoEmpresa;
	
	@Column(name = "SEQUENCIAL_ARQUIVO_COBRANCA")
	private String sequencialArquivoCobranca;

	@Column(name = "CONVENIO")
	private String convenio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public Long getAgencia() {
		return agencia;
	}

	public void setAgencia(Long agencia) {
		this.agencia = agencia;
	}

	public String getDvAgencia() {
		return dvAgencia;
	}

	public void setDvAgencia(String dvAgencia) {
		this.dvAgencia = dvAgencia;
	}

	public Long getConta() {
		return conta;
	}

	public void setConta(Long conta) {
		this.conta = conta;
	}

	public String getDvConta() {
		return dvConta;
	}

	public void setDvConta(String dvConta) {
		this.dvConta = dvConta;
	}

	public Pessoa getPessoaJuridicaCedente() {
		return pessoaJuridicaCedente;
	}

	public void setPessoaJuridicaCedente(Pessoa pessoaJuridicaCedente) {
		this.pessoaJuridicaCedente = pessoaJuridicaCedente;
	}

	public String getCodigoCedente() {
		return codigoCedente;
	}

	public void setCodigoCedente(String codigoCedente) {
		this.codigoCedente = codigoCedente;
	}

	public String getDigitoCodigoCedente() {
		return digitoCodigoCedente;
	}

	public void setDigitoCodigoCedente(String digitoCodigoCedente) {
		this.digitoCodigoCedente = digitoCodigoCedente;
	}

	public Integer getCarteira() {
		return carteira;
	}

	public void setCarteira(Integer carteira) {
		this.carteira = carteira;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getInstrucoes1() {
		return instrucoes1;
	}

	public void setInstrucoes1(String instrucoes1) {
		this.instrucoes1 = instrucoes1;
	}

	public String getInstrucoes2() {
		return instrucoes2;
	}

	public void setInstrucoes2(String instrucoes2) {
		this.instrucoes2 = instrucoes2;
	}

	public String getInstrucoes3() {
		return instrucoes3;
	}

	public void setInstrucoes3(String instrucoes3) {
		this.instrucoes3 = instrucoes3;
	}

	public String getInstrucoes4() {
		return instrucoes4;
	}

	public void setInstrucoes4(String instrucoes4) {
		this.instrucoes4 = instrucoes4;
	}

	public BigDecimal getJuros() {
		return juros;
	}

	public void setJuros(BigDecimal juros) {
		this.juros = juros;
	}

	public BigDecimal getMulta() {
		return multa;
	}

	public void setMulta(BigDecimal multa) {
		this.multa = multa;
	}

	public BigDecimal getVrMulta() {
		return vrMulta;
	}

	public void setVrMulta(BigDecimal vrMulta) {
		this.vrMulta = vrMulta;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public boolean isExibirValorMonetario() {
		return exibirValorMonetario;
	}

	public void setExibirValorMonetario(boolean exibirValorMonetario) {
		this.exibirValorMonetario = exibirValorMonetario;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((Banco) obj).id);
	}

	public String getSequencialArquivoCobranca() {
		return sequencialArquivoCobranca;
	}

	public void setSequencialArquivoCobranca(String sequencialArquivoCobranca) {
		this.sequencialArquivoCobranca = sequencialArquivoCobranca;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
}
