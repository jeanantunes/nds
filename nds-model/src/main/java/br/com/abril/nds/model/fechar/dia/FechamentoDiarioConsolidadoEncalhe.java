package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE")
@SequenceGenerator(name = "FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class FechamentoDiarioConsolidadoEncalhe implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@Column(name = "VALOR_LOGICO")
	private BigDecimal valorLogico;
	
	@Column(name = "VALOR_FISICO")
	private BigDecimal valorFisico;
	
	@Column(name = "VALOR_JURAMENTADO")
	private BigDecimal valorJuramentado;
	
	@Column(name = "VALOR_VENDA")
	private BigDecimal valorVenda;
	
	@Column(name = "VALOR_SOBRA_EM")
	private BigDecimal valorSobraEM;
	
	@Column(name = "VALOR_FALTA_EM")
	private BigDecimal valorFaltaEM;
	
	@Column(name = "SALDO")
	private BigDecimal saldo;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@OneToMany(mappedBy = "fechamentoDiarioConsolidadoEncalhe")
	private List<FechamentoDiarioLancamentoEncalhe> lancamentosReparte;
	
	@OneToMany(mappedBy = "fechamentoDiarioConsolidadoEncalhe")
	private List<FechamentoDiarioMovimentoVendaEncalhe> movimentoVendaEncalhes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorLogico() {
		return valorLogico;
	}

	public void setValorLogico(BigDecimal valorLogico) {
		this.valorLogico = valorLogico;
	}

	public BigDecimal getValorFisico() {
		return valorFisico;
	}

	public void setValorFisico(BigDecimal valorFisico) {
		this.valorFisico = valorFisico;
	}

	public BigDecimal getValorJuramentado() {
		return valorJuramentado;
	}

	public void setValorJuramentado(BigDecimal valorJuramentado) {
		this.valorJuramentado = valorJuramentado;
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public BigDecimal getValorSobraEM() {
		return valorSobraEM;
	}

	public void setValorSobraEM(BigDecimal valorSobraEM) {
		this.valorSobraEM = valorSobraEM;
	}

	public BigDecimal getValorFaltaEM() {
		return valorFaltaEM;
	}

	public void setValorFaltaEM(BigDecimal valorFaltaEM) {
		this.valorFaltaEM = valorFaltaEM;
	}

	public FechamentoDiario getFechamentoDiario() {
		return fechamentoDiario;
	}

	public void setFechamentoDiario(FechamentoDiario fechamentoDiario) {
		this.fechamentoDiario = fechamentoDiario;
	}

	public List<FechamentoDiarioLancamentoEncalhe> getLancamentosReparte() {
		return lancamentosReparte;
	}

	public void setLancamentosReparte(
			List<FechamentoDiarioLancamentoEncalhe> lancamentosReparte) {
		this.lancamentosReparte = lancamentosReparte;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public List<FechamentoDiarioMovimentoVendaEncalhe> getMovimentoVendaEncalhes() {
		return movimentoVendaEncalhes;
	}

	public void setMovimentoVendaEncalhes(
			List<FechamentoDiarioMovimentoVendaEncalhe> movimentoVendaEncalhes) {
		this.movimentoVendaEncalhes = movimentoVendaEncalhes;
	}
	
	
}
