package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.DescontoLogistica;

public interface DescontoLogisticaService {

	/**
	 * Obtem Desconto Logistica por tipoDesconto
	 * @param tipoDesconto
	 * @return DescontoLogistica
	 */
	DescontoLogistica obterPorTipoDesconto(Integer tipoDesconto);
	
}
