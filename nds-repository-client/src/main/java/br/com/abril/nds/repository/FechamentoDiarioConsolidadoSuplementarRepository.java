package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoSuplementar;

public interface FechamentoDiarioConsolidadoSuplementarRepository extends Repository<FechamentoDiarioConsolidadoSuplementar, Long> {
	
	ResumoSuplementarFecharDiaDTO obterResumoGeralSuplementar(Date dataFechamento);
}
