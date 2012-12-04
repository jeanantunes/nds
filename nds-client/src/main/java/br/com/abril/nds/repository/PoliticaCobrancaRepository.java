package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
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
	
	PoliticaCobranca buscarPoliticaCobrancaPrincipal();
	
	List<PoliticaCobranca> obterPoliticasCobranca(List<TipoCobranca> tiposCobranca);
	
	/**
	 * Obtém Lista de políticas de cobrança para os parametros
	 * @return List<PoliticaCobranca>
	 */
	List<PoliticaCobranca> obterPoliticasCobranca(FiltroParametrosCobrancaDTO filtro);
	
	/**
	 * Obtém Quantidade de políticas de cobrança para os parametros
	 * @return int
	 */
	int obterQuantidadePoliticasCobranca(FiltroParametrosCobrancaDTO filtro);

	/**
	 * Desativa uma politica de cobrança
	 * @param idPolitica
	 */
	void desativarPoliticaCobranca(long idPolitica);
}
