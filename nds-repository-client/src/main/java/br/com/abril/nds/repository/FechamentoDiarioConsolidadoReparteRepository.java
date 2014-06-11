package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoReparte;

public interface FechamentoDiarioConsolidadoReparteRepository extends Repository<FechamentoDiarioConsolidadoReparte,Long> {
	
	SumarizacaoReparteDTO obterSumarizacaoReparte(Date data);
}
