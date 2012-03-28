package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.model.movimentacao.CotaAusente;

public interface CotaAusenteRepository extends Repository<CotaAusente,Long> {
	
	 List<CotaAusenteDTO> obterCotasAusentes(Date data, Long idCota, CotaAusenteDTO cotaAusenteDTO);

}
