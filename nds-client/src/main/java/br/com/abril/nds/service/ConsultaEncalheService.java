package br.com.abril.nds.service;

import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;

public interface ConsultaEncalheService {
	
	/**
	 * Executa a pesquisa de ConsultaEncalhe. É retornado um objeto que 
	 * contém a lista de resultados e uma sumarização do mesmo.
	 * 
	 * @param filtro
	 * 
	 * @return InfoConsultaEncalhe
	 */
	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro);
	
}
