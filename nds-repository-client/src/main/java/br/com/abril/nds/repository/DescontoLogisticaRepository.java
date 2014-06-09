package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.DescontoLogistica;

public interface DescontoLogisticaRepository extends Repository<DescontoLogistica, Long> {
	
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
	public DescontoLogistica obterDescontoLogistica(Integer tipoDesconto, Long idFornecedor, Date dataVigencia, BigDecimal percentualDesconto);
	
	/**
     * Obtem Desconto Logistica vigente
     * 
     * @param tipoDesconto
     * @param idFornecedor
     * @param dataVigencia
     * 
     * @return DescontoLogistica
     */
	public DescontoLogistica obterDescontoLogisticaVigente(Integer tipoDesconto, Long idFornecedor, Date dataVigencia);
	
}
