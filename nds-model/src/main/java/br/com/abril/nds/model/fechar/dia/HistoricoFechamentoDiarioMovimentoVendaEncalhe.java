package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HISTORICO_FECHAMENTO_DIARIO_MOVIMENTO_VENDAS_ENCALHE")
public class HistoricoFechamentoDiarioMovimentoVendaEncalhe extends HistoricoFechamentoDiarioMovimentoVenda implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_ENCALHE_ID")
	private HistoricoFechamentoDiarioConsolidadoEncalhe historicoConsolidadoEncalhe;

	public HistoricoFechamentoDiarioConsolidadoEncalhe getHistoricoConsolidadoEncalhe() {
		return historicoConsolidadoEncalhe;
	}

	public void setHistoricoConsolidadoEncalhe(
			HistoricoFechamentoDiarioConsolidadoEncalhe historicoConsolidadoEncalhe) {
		this.historicoConsolidadoEncalhe = historicoConsolidadoEncalhe;
	}
	
}
