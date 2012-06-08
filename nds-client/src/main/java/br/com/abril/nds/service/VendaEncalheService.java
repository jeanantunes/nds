package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.SlipVendaEncalheDTO;

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
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	 * @return List<SlipVendaEncalheDTO>
	 */
	 List<SlipVendaEncalheDTO> obtemDadosSlip(long idCota, Date dataInicio, Date dataFim);
		
	 
	 /**
	 * Gera Array de Bytes do Slip de Venda de Encalhe
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	  * @return byte[]
	 */
	 byte[] geraImpressaoVendaEncalhe(long idCota, Date dataInicio, Date dataFim);
	 
	 
	 /**
	 * Gera Array de Bytes do Slip de Venda de Suplementar
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	  * @return byte[]
	 */
	 byte[] geraImpressaoVendaSuplementar(long idCota, Date dataInicio, Date dataFim);
	
}
