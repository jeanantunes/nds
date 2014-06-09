package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoEncalhe;

public interface FechamentoDiarioConsolidadoEncalheRepository extends Repository<FechamentoDiarioConsolidadoEncalhe, Long> {
	
	ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataFechamento);
}
