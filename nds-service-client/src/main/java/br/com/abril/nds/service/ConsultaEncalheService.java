package br.com.abril.nds.service;

import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;

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
	
	public InfoConsultaEncalheDetalheDTO pesquisarEncalheDetalhe(FiltroConsultaEncalheDetalheDTO filtro);
	
	/**
	 * Gera arquivo Documento Slip.
	 * 
	 * @param filtro
	 * @return
	 */
	public byte[] gerarDocumentosConferenciaEncalhe(FiltroConsultaEncalheDTO filtro);
	
	InfoConsultaEncalheDTO pesquisarReparte(FiltroConsultaEncalheDTO filtro);
	
}
