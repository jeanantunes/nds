package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.DescontoLogistica;

public interface DescontoLogisticaRepository extends Repository<DescontoLogistica, Long> {
	
	/**
	 * Obtem Desconto Logistica por tipoDesconto
	 * @param tipoDesconto
	 * @return DescontoLogistica
	 */
	public DescontoLogistica obterPorTipoDesconto(Integer tipoDesconto);
	
}
