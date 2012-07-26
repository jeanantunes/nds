package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;


/**
 * Interface de serviços referentes a serviços de chamadade encalhe. 
 *   
 * 
 * @author Discover Technology
 */
public interface ChamadaEncalheService {
	
	List<CotaEmissaoDTO> obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro);

	List<CotaEmissaoDTO> obterDadosImpressaoEmissaoChamadasEncalhe(
			FiltroEmissaoCE filtro);
	
}
