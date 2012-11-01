package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.com.abril.nds.model.cadastro.TipoCobranca;

@Entity
@Table(name="HISTORICO_FECHAMENTO_DIARIO_RESUMO_CONSOLIDADO_DIVIDA")
@SequenceGenerator(name="HISTORICO_FECHAMENTO_DIARIO_RESUMO_CONSOLIDADO_DIVIDA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoFechamentoDiarioResumoConsolidadoDivida implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "HISTORICO_FECHAMENTO_DIARIO_RESUMO_CONSOLIDADO_DIVIDA_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_ID")
	private HistoricoFechamentoDiarioConsolidadoDivida historicoConsolidadoDivida;
	
	@Column(name="FORMA_PAGAMENTO")
	@Enumerated(EnumType.STRING)
	private TipoCobranca tipoCobranca;
	
	@Column(name="VALOR_TOTAL")
	private BigDecimal valorTotal;
	
	@Column(name="VALOR_PAGO")
	private BigDecimal valorPago;
	
	@Column(name="VALOR_INADIMPLENTE")
	private BigDecimal valorInadimplencia;
	
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

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public BigDecimal getValorInadimplencia() {
		return valorInadimplencia;
	}

	public void setValorInadimplencia(BigDecimal valorInadimplencia) {
		this.valorInadimplencia = valorInadimplencia;
	}

	public HistoricoFechamentoDiarioConsolidadoDivida getHistoricoConsolidadoDivida() {
		return historicoConsolidadoDivida;
	}

	public void setHistoricoConsolidadoDivida(
			HistoricoFechamentoDiarioConsolidadoDivida historicoConsolidadoDivida) {
		this.historicoConsolidadoDivida = historicoConsolidadoDivida;
	}
	
	
}
