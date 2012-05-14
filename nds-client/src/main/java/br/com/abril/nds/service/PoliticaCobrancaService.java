package br.com.abril.nds.service;
import java.util.List;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Banco}
 * 
 * @author Discover Technology
 */
public interface PoliticaCobrancaService {

	/**
	 * Obtém dados da politica de cobrança (PoliticaCobranca e a Forma de Cobranca vinculada)
	 * @param filtro
	 * @return List<ParametroCobrancaVO>
	 */
	List<ParametroCobrancaVO> obterDadosPoliticasCobranca(FiltroParametrosCobrancaDTO filtro);

	/**
	 * Obtém quantidade de politicas de cobrança (PoliticaCobranca e a Forma de Cobranca vinculada)
	 * @param filtro
	 * @return List<ParametroCobrancaVO>
	 */
	int obterQuantidadePoliticasCobranca(FiltroParametrosCobrancaDTO filtro);
	
	
	/**
	 * Obtém politica de cobrança principal do Distribuidor
	 * @return {@link br.com.abril.nds.model.cadastro.PoliticaCobranca}
	 */
	PoliticaCobranca obterPoliticaCobrancaPrincipal();

}
