package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FECHAMENTO_DIARIO_MOVIMENTO_VENDAS_ENCALHE")
public class FechamentoDiarioMovimentoVendaEncalhe extends FechamentoDiarioMovimentoVenda implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID")
	private FechamentoDiarioConsolidadoEncalhe fechamentoDiarioConsolidadoEncalhe;

	public FechamentoDiarioConsolidadoEncalhe getFechamentoDiarioConsolidadoEncalhe() {
		return fechamentoDiarioConsolidadoEncalhe;
	}

	public void setFechamentoDiarioConsolidadoEncalhe(
			FechamentoDiarioConsolidadoEncalhe fechamentoDiarioConsolidadoEncalhe) {
		this.fechamentoDiarioConsolidadoEncalhe = fechamentoDiarioConsolidadoEncalhe;
	}
	
}
