package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsolidadoDivida;

public interface FechamentoDiarioResumoConsolidadoDividaRepository extends Repository<FechamentoDiarioResumoConsolidadoDivida, Long> {
	
	List<SumarizacaoDividasDTO> sumarizacaoDividas(Date dataFechamento, TipoDivida tipoDivida);
}
