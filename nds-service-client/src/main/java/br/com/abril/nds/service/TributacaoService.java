package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiroProduto;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalTributacao;

/**
 * 
 * @author Diego Fernandes
 * 
 */
public interface TributacaoService {
	
	/**
	 * Calcula tributos de produto com base nos paramentros e os dados cadastros na entidade de {@link NotaFiscalTributacao}
	
	 * 
	 * @param codigoEmpresa
	 * @param tipoOperacao
	 * @param ufOrigem
	 * @param ufDestino
	 * @param naturezaOperacao
	 * @param codigoNaturezaOperacao
	 * @param codigoNBM
	 * @param dataVigencia
	 * @param cstICMS
	 * @param valorItem
	 * @return
	 */
	public abstract EncargoFinanceiroProduto calcularTributoProduto(String codigoEmpresa, TipoOperacao tipoOperacao, String ufOrigem,
			String ufDestino, int naturezaOperacao, String codigoNaturezaOperacao, String codigoNBM, Date dataVigencia,
			String cstICMS, BigDecimal valorItem);

}
