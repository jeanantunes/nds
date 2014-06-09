package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota.TipoSituacaoCota;

public interface FechamentoDiarioCotaRepository extends Repository<FechamentoDiarioCota, Long> {
	
	List<CotaResumoDTO> obterCotas(Date dataFechamento, TipoSituacaoCota tipoSituacaoCota);
}
