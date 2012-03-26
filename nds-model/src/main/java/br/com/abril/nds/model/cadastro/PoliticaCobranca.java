package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	@JoinColumn(name = "FORMA_COBRANCA_ID")
	private FormaCobranca formaCobranca;
	
	@Column(name = "NUM_INADIMPLENCIA_SUSP", nullable = false)
	private int inadimplenciasSuspencao;
	
	@OneToOne(mappedBy = "politicaCobranca")
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
	
	@Column(name = "NUM_DIAS_POSTERGADO")
	private int numeroDiasPostergado;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getInadimplenciasSuspencao() {
		return inadimplenciasSuspencao;
	}
	
	public void setInadimplenciasSuspencao(int inadimplenciasSuspencao) {
		this.inadimplenciasSuspencao = inadimplenciasSuspencao;
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
	
	public int getNumeroDiasPostergado() {
		return numeroDiasPostergado;
	}
	
	public void setNumeroDiasPostergado(int numeroDiasPostergado) {
		this.numeroDiasPostergado = numeroDiasPostergado;
	}
	
}
