package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;


/**
 * Interface que define os serviços referentes
 * ao cadastro de garantias da cota.
 * 
 * @author Discover Technology
 */
public interface CotaGarantiaService {
	
	
	
	
	
	/**
	 * Salva os imoveis de garantia da cota.
	 * @param cotaGarantiaImovel
	 */
	public void salvaImoveis(CotaGarantiaImovel cotaGarantiaImovel);

}
