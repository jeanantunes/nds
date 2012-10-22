package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotasImpressaoNfeDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public interface ImpressaoNFeRepository extends Repository<NotaFiscal, Long>  {

	/**
	 * Retorna uma lista de NF-e's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public List<CotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna a quantidade de NF-e's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public Integer buscarCotasParaImpressaoNFeQtd(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna as NF-e's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	List<NfeDTO> buscarNFes(FiltroImpressaoNFEDTO filtro);
	
}
