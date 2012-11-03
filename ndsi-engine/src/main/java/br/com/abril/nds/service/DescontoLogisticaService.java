package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.DescontoLogistica;

public interface DescontoLogisticaService {

	/**
	 * Obtem Desconto Logistica por tipoDesconto
	 * @param tipoDesconto
	 * @return DescontoLogistica
	 */
	DescontoLogistica obterPorTipoDesconto(Integer tipoDesconto);

	/**
	 * Obtém uma lista de todos os Desconto Logísticas
	 * @return List<DescontoLogistica>
	 */
	List<DescontoLogistica> obterTodos();
	
}
