package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoCota;

public interface FechamentoDiarioConsolidadoCotaRepository extends Repository<FechamentoDiarioConsolidadoCota, Long> {
	
	FechamentoDiarioConsolidadoCota obterResumoConsolidadoCotas(Date dataFechamento);
}
