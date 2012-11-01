package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA")
@SequenceGenerator(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoFechamentoDiarioConsolidadoDivida implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_DIVIDA_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FECHAMENTO_DIARIO_ID")
	private FechamentoDiario fechamentoDiario;
	
	@Column(name="TIPO_DIVIDA")
	@Enumerated(EnumType.STRING)
	private TipoDividaFechamentoDia tipoDivida;
	
	@OneToMany(mappedBy = "historicoConsolidadoDivida")
	private List<HistoricoFechamentoDiarioDivida> historicoDividas;
	
	@OneToMany(mappedBy = "historicoConsolidadoDivida")
	private List<HistoricoFechamentoDiarioResumoConsolidadoDivida> historicoResumoConsolidadoDividas;
	
	public Long getId() {
		return id;
	}
	
	public enum TipoDividaFechamentoDia {	
		A_VENCER, A_RECEBER
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

	public List<HistoricoFechamentoDiarioDivida> getHistoricoDividas() {
		return historicoDividas;
	}

	public void setHistoricoDividas(
			List<HistoricoFechamentoDiarioDivida> historicoDividas) {
		this.historicoDividas = historicoDividas;
	}

	public List<HistoricoFechamentoDiarioResumoConsolidadoDivida> getHistoricoResumoConsolidadoDividas() {
		return historicoResumoConsolidadoDividas;
	}

	public void setHistoricoResumoConsolidadoDividas(
			List<HistoricoFechamentoDiarioResumoConsolidadoDivida> historicoResumoConsolidadoDividas) {
		this.historicoResumoConsolidadoDividas = historicoResumoConsolidadoDividas;
	}

	public TipoDividaFechamentoDia getTipoDivida() {
		return tipoDivida;
	}

	public void setTipoDivida(TipoDividaFechamentoDia tipoDivida) {
		this.tipoDivida = tipoDivida;
	}
	
	
}
