package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Produto;

public interface DescontoLogisticaService {

	/**
	 * Obtem Desconto Logistica 
	 * 
	 * @param tipoDesconto
	 * @param idFornecedor
	 * @param dataVigencia
	 * @param percentualDesconto
	 * 
	 * @return DescontoLogistica
	 */
	DescontoLogistica obterDescontoLogistica(Integer tipoDesconto, Long idFornecedor, Date dataVigencia, BigDecimal percentualDesconto);

	/**
     * Obtem Desconto Logistica vigente 
     * 
     * @param tipoDesconto
     * @param idFornecedor
     * @param dataVigencia
     * 
     * @return DescontoLogistica
     */
	DescontoLogistica obterDescontoLogisticaVigente(Integer tipoDesconto, Long idFornecedor, Date dataVigencia);
	
	/**
	 * Obtém uma lista de todos os Desconto Logísticas
	 * @return List<DescontoLogistica>
	 */
	List<DescontoLogistica> obterTodos();
	
	
}
