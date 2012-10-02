package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;

public interface ControleConferenciaEncalheCotaRepository  extends Repository<ControleConferenciaEncalheCota,Long> {
	
	/**
	 * Obtém registro de ControleConferenciaEncalheCota referente a uma 
	 * cota e dataOperação.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * 
	 * @return ControleConferenciaEncalheCota
	 */
	public ControleConferenciaEncalheCota obterControleConferenciaEncalheCota(Integer numeroCota, Date dataOperacao);

	/**
	 * Obtém todas as ControleConferenciaEncalheCota pelo FiltroConsultaEncalheDTO.
	 * 
	 * @param filtro
	 * @return List<ControleConferenciaEncalheCota>
	 */
	public List<ControleConferenciaEncalheCota> obterControleConferenciaEncalheCotaPorFiltro(FiltroConsultaEncalheDTO filtro);	
}
