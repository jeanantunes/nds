package br.com.abril.nds.service;

import br.com.abril.nds.dto.SlipDTO;

/**
 * 
 * Interface de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.VendaEncalhe}  
 * 
 * @author Discover Technology
 *
 */
public interface VendaEncalheService {
	
	/**
	 * Obtém dados da venda encalhe por id
	 * @param idVendaEncalhe
	 * @return SlipDTO
	 */
	 SlipDTO obtemDadosSlip(long idVendaEncalhe);
	 
	 /**
	  * Gera Array de Bytes do Slip de Venda de Encalhe
	  * @param idVendaEncalhe
	  * @return byte[]
	  */
	 byte[] geraImpressaoVendaEncalhe(Long idVendaEncalhe);
	
}
