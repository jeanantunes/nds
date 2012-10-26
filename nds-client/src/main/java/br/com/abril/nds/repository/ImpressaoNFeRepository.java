package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotasImpressaoNfeDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
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
	 * Retorna uma lista de NE's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public List<CotasImpressaoNfeDTO> buscarCotasParaImpressaoNotaEnvio(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna a quantidade de NE's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public Integer buscarCotasParaImpressaoNotaEnvioQtd(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna uma lista de NF's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public List<NotaFiscal> buscarNotasPorCotaParaImpressaoNFe(Cota cota, FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Retorna uma lista de NE's baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public List<NotaEnvio> buscarNotasEnvioPorCotaParaImpressaoNFe(Cota cota, FiltroImpressaoNFEDTO filtro);
		
}
