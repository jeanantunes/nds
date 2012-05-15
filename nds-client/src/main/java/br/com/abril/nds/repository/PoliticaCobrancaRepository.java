package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.PoliticaCobranca}  
 * 
 * @author Discover Technology
 *
 */
public interface PoliticaCobrancaRepository extends Repository<PoliticaCobranca,Long> {

	/**
	 * Obtém uma politica de cobrança de acordo com o tipo de cobrança.
	 * 
	 * @param tipoCobranca - tipo de cobranca
	 * 
	 * @return {@link PoliticaCobranca}
	 */
	PoliticaCobranca obterPorTipoCobranca(TipoCobranca tipoCobranca);
	
	PoliticaCobranca buscarPoliticaCobrancaPorDistribuidor();

}
