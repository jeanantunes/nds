package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "POLITICA_COBRANCA")
@SequenceGenerator(name="POLITICA_COBRANCA_SEQ", initialValue = 1, allocationSize = 1)
public class PoliticaCobranca {
	
	@Id
	@GeneratedValue(generator = "POLITICA_COBRANCA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "FORMA_COBRANCA_ID", unique = true)
	private FormaCobranca formaCobranca;

	@ManyToOne
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	@Column(name = "ACUMULA_DIVIDA", nullable = false)
	private boolean acumulaDivida;
	
	@Column(name = "ACEITA_BAIXA_PGTO_MAIOR")
	private boolean aceitaBaixaPagamentoMaior;
	
	@Column(name = "ACEITA_BAIXA_PGTO_MENOR")
	private boolean aceitaBaixaPagamentoMenor;
	
	@Column(name = "ACEITA_BAIXA_PGTO_VENCIDO")
	private boolean aceitaBaixaPagamentoVencido;	
	
	@Column(name = "NUM_DIAS_NOVA_COBRANCA")
	private int numeroDiasNovaCobranca;
	
	@Column(name = "ASSUNTO_EMAIL_COBRANCA")
	private String assuntoEmailCobranca;
	
	@Column(name = "MENSAGEM_EMAIL_COBRANCA")
	private String mensagemEmailCobranca;
	
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;
	
	@Column(name = "ATIVO", nullable = false)
	private boolean ativo;
	
	@Column(name = "UNIFICA_COBRANCA", nullable = false)
	private boolean unificaCobranca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FORMA_EMISSAO")
	private FormaEmissao formaEmissao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Distribuidor getDistribuidor() {
		return distribuidor;
	}
	
	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}
	
	public boolean isAcumulaDivida() {
		return acumulaDivida;
	}
	
	public void setAcumulaDivida(boolean acumulaDivida) {
		this.acumulaDivida = acumulaDivida;
	}

	public boolean isAceitaBaixaPagamentoMaior() {
		return aceitaBaixaPagamentoMaior;
	}

	public void setAceitaBaixaPagamentoMaior(boolean aceitaBaixaPagamentoMaior) {
		this.aceitaBaixaPagamentoMaior = aceitaBaixaPagamentoMaior;
	}

	public boolean isAceitaBaixaPagamentoMenor() {
		return aceitaBaixaPagamentoMenor;
	}

	public void setAceitaBaixaPagamentoMenor(boolean aceitaBaixaPagamentoMenor) {
		this.aceitaBaixaPagamentoMenor = aceitaBaixaPagamentoMenor;
	}

	public boolean isAceitaBaixaPagamentoVencido() {
		return aceitaBaixaPagamentoVencido;
	}

	public void setAceitaBaixaPagamentoVencido(boolean aceitaBaixaPagamentoVencido) {
		this.aceitaBaixaPagamentoVencido = aceitaBaixaPagamentoVencido;
	}
	
	public FormaCobranca getFormaCobranca() {
		return formaCobranca;
	}
	
	public void setFormaCobranca(FormaCobranca formaCobranca) {
		this.formaCobranca = formaCobranca;
	}
	
	public int getNumeroDiasNovaCobranca() {
		return numeroDiasNovaCobranca;
	}
	
	public void setNumeroDiasNovaCobranca(int numeroDiasNovaCobranca) {
		this.numeroDiasNovaCobranca = numeroDiasNovaCobranca;
	}

	public String getAssuntoEmailCobranca() {
		return assuntoEmailCobranca;
	}

	public void setAssuntoEmailCobranca(String assuntoEmailCobranca) {
		this.assuntoEmailCobranca = assuntoEmailCobranca;
	}

	public String getMensagemEmailCobranca() {
		return mensagemEmailCobranca;
	}

	public void setMensagemEmailCobranca(String mensagemEmailCobranca) {
		this.mensagemEmailCobranca = mensagemEmailCobranca;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public boolean isUnificaCobranca() {
		return unificaCobranca;
	}

	public void setUnificaCobranca(boolean unificaCobranca) {
		this.unificaCobranca = unificaCobranca;
	}

	public FormaEmissao getFormaEmissao() {
		return formaEmissao;
	}

	public void setFormaEmissao(FormaEmissao formaEmissao) {
		this.formaEmissao = formaEmissao;
	}

	
}
