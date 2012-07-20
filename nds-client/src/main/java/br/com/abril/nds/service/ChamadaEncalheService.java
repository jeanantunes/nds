package br.com.abril.nds.service;

import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;


/**
 * Interface de serviços referentes a serviços de chamadade encalhe. 
 *   
 * 
 * @author Discover Technology
 */
public interface ChamadaEncalheService {
	
	void obterDadosEmissaoChamadasEncalhe(FiltroEmissaoCE filtro);
	
}
