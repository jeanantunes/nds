package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.model.movimentacao.RateioCotaAusente;

public interface CotaAusenteService {
	
	void declararCotaAusente(Integer numCota, Date data, List<RateioCotaAusente> listaDeRateio, Long idUsuario);
	
	List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtroCotaAusenteDTO);
	
	Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro);
	
	void cancelarCotaAusente(Long idCotaAusente, Long idUsuario);

}
