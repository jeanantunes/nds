package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.FormaCobranca;

public interface FormaCobrancaService {

	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados
	 * @param numeroCota
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
    FormaCobranca obterFormaCobrancaCota(Integer numeroCota, List<Long> fornecedoresId, Date data, BigDecimal valor);

    /**
	 * Obtem FormaCobranca do Distribuidor com os Parâmetros passados
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
    FormaCobranca obterFormaCobrancaDistribuidor(List<Long> fornecedoresId, Date data, BigDecimal valor);
    
    /**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados, caso não encontre, busca FormaCobranca do Distribuidor 
	 * @param numeroCota
	 * @param fornecedoresId
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
    FormaCobranca obterFormaCobranca(Integer numeroCota, List<Long> fornecedoresId, Date data, BigDecimal valor);
    
    /**
     * Obtem FormaCobranca principal da Cota
     * @param idCota
     * @return FormaCobranca
     */
    FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota);
    
    /**
     * Obtem FormaCobranca principal do Distribuidor
     * @return FormaCobranca
     */
    FormaCobranca obterFormaCobrancaPrincipalDistribuidor();
}
