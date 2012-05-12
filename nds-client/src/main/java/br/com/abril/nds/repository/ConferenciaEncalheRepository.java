package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;

public interface ConferenciaEncalheRepository extends Repository<ConferenciaEncalhe, Long> { 
	
	/**
	 * Obtém uma lista de conferenciaEncalheDTO específicos de uma cota
	 * e relacionados a um registro de ControleConferenciaEncalheCota.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * 
	 * @return List - ConferenciaEncalheDTO
	 */
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota);
	
	
	/**
	 * Obtém o valor total das conferencias de encalhe de uma cota relacionadas
	 * a uma registro de ControleConferenciaEncalheCota.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterValorTotalConferenciaEncalheCota(Long idControleConferenciaEncalheCota);

	
}
