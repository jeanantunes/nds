package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.model.movimentacao.RateioCotaAusente;

public interface CotaAusenteService {
	
	void declararCotaAusente(Long idCota, Date data, List<RateioCotaAusente> listaDeRateio);
	
	List<CotaAusenteDTO> obterCotasAusentes(Date data, Long idCota, CotaAusenteDTO cotaAusenteDTO);

}
