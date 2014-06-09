package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FECHAMENTO_DIARIO_MOVIMENTO_VENDAS_SUPLEMENTAR")
public class FechamentoDiarioMovimentoVendaSuplementar extends FechamentoDiarioMovimentoVenda implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "FECHAMENTO_DIARIO_CONSOLIDADO_SUPLEMENTAR_ID")
	private FechamentoDiarioConsolidadoSuplementar fechamentoDiarioConsolidadoSuplementar;

	public FechamentoDiarioConsolidadoSuplementar getFechamentoDiarioConsolidadoSuplementar() {
		return fechamentoDiarioConsolidadoSuplementar;
	}

	public void setFechamentoDiarioConsolidadoSuplementar(
			FechamentoDiarioConsolidadoSuplementar historicoConsolidadoSuplementar) {
		this.fechamentoDiarioConsolidadoSuplementar = historicoConsolidadoSuplementar;
	}
}
