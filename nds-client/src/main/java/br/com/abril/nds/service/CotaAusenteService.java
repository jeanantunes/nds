package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.service.exception.TipoMovimentoEstoqueInexistente;

public interface CotaAusenteService {
	
	void ratearCotaAusente(Integer numCota, Date data, List<MovimentoEstoqueCotaDTO> listaMovimentosDTO, Long idUsuario) throws TipoMovimentoEstoqueInexistente;
	
	void declararCotaAusente(Integer numCota, Date data, Long idUsuario) throws TipoMovimentoEstoqueInexistente;
	
	List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtroCotaAusenteDTO);
	
	Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro);
	
	void cancelarCotaAusente(Long idCotaAusente, Long idUsuario);

}
