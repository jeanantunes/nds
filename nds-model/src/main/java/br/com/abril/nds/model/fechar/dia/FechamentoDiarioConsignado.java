package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FECHAMENTO_DIARIO_RESUMO_CONSIGNADO")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_RESUMO_CONSIGNADO_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_RESUMO", discriminatorType = DiscriminatorType.STRING)
public abstract class  FechamentoDiarioConsignado implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_RESUMO_CONSIGNADO_SEQ")
    @Column(name = "ID")
    private Long id;
		
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@Column(name="SALDO_ANTERIOR")
	private BigDecimal saldoAnterior;
	
	@Column(name="VALOR_ENTRADA")
	private BigDecimal valorEntradas;
	
	@Column(name="VALOR_SAIDA")
	private BigDecimal valorSaidas;
	
	@Column(name="SALDO_ATUAL")
	private BigDecimal saldoAtual;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FechamentoDiario getFechamentoDiario() {
		return fechamentoDiario;
	}

	public void setFechamentoDiario(FechamentoDiario fechamentoDiario) {
		this.fechamentoDiario = fechamentoDiario;
	}

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public BigDecimal getValorEntradas() {
		return valorEntradas;
	}

	public void setValorEntradas(BigDecimal valorEntradas) {
		this.valorEntradas = valorEntradas;
	}

	public BigDecimal getValorSaidas() {
		return valorSaidas;
	}

	public void setValorSaidas(BigDecimal valorSaidas) {
		this.valorSaidas = valorSaidas;
	}

	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}

	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}
	
	
	
}
