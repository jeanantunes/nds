package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BANCO")
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
	@Column(name = "AGENCIA", nullable = false)
	private Long agencia;
	@Column(name = "DV_AGENCIA")
	private String dvAgencia;
	@Column(name = "CONTA", nullable = false)
	private Long conta;
	@Column(name = "DV_CONTA")
	private String dvConta;
	@Column(name = "CODIGO_CEDENTE", nullable = false)
	private String codigoCedente;
	@ManyToOne
	@JoinColumn(name = "CARTEIRA_ID")
	private Carteira carteira;
	@Column(name = "MOEDA", nullable = false)
	private Moeda moeda;
	@Column(name = "ATIVO", nullable = false)
	private boolean ativo;
	@Column(name = "INSTRUCOES", nullable = false)
	private String instrucoes;
	@Column(name = "JUROS")
	private BigDecimal juros;
	@Column(name = "MULTA")
	private BigDecimal multa;
	
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
	
	public String getCodigoCedente() {
		return codigoCedente;
	}
	
	public void setCodigoCedente(String codigoCedente) {
		this.codigoCedente = codigoCedente;
	}
	
	public Carteira getCarteira() {
		return carteira;
	}
	
	public void setCarteira(Carteira carteira) {
		this.carteira = carteira;
	}
	
	public Moeda getMoeda() {
		return moeda;
	}
	
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getInstrucoes() {
		return instrucoes;
	}
	
	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
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
	
}
