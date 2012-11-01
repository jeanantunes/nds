package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "HISTORICO_FECHAMENTO_DIARIO_MOVIMENTO_VENDAS_SUPLEMENTAR")
public class HistoricoFechamentoDiarioMovimentoVendaSuplementar extends HistoricoFechamentoDiarioMovimentoVenda implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID")
	private HistoricoFechamentoDiarioConsolidadoSuplementar historicoConsolidadoSuplementar;

	public HistoricoFechamentoDiarioConsolidadoSuplementar getHistoricoConsolidadoSuplementar() {
		return historicoConsolidadoSuplementar;
	}

	public void setHistoricoConsolidadoSuplementar(
			HistoricoFechamentoDiarioConsolidadoSuplementar historicoConsolidadoSuplementar) {
		this.historicoConsolidadoSuplementar = historicoConsolidadoSuplementar;
	}
}
