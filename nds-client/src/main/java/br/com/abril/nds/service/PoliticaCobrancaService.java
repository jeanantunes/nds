package br.com.abril.nds.service;
import java.util.List;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Banco}
 * 
 * @author Discover Technology
 */
public interface PoliticaCobrancaService {
	
	/**
	 * Método responsável por obter formas de emissao para preencher combo da camada view
	 * @return comboFormasEmissao: formas de emissão cadastrados
	 */
    List<ItemDTO<FormaEmissao, String>> getComboFormasEmissao();
    
    /**
	 * REGRA POLÍTICA DE COBRANÇA: Método responsável por obter formas de emissao dependento do Tipo de Cobrança para preencher combo da camada view
	 * @return comboFormasEmissao: formas de emissão cadastrados
	 */
    List<ItemDTO<FormaEmissao, String>> getComboFormasEmissaoTipoCobranca(TipoCobranca tipoCobranca);
    
    /**
     * Obtém todas as Politicas de Cobrança
     * @return List<PoliticaCobranca>
     */
    List<PoliticaCobranca> obterPoliticasCobranca();

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
	
	/**
	 * Método responsável por postar o parametro de cobranca no banco
	 * @param parametroCobranca
	 */
	void postarPoliticaCobranca(ParametroCobrancaDTO parametroCobrancaDTO);
	
	
	/**
	 * Método responsável por obter os dados da politica de cobranca
	 * @param idPolitica: ID da politica de cobranca
	 * @return Data Transfer Object com os dados da politica de cobranca
	 */
	ParametroCobrancaDTO obterDadosPoliticaCobranca(Long idPolitica);
	
	/**
	 * Desativa uma politica de cobrança
	 * @param idPolitica
	 */
	void dasativarPoliticaCobranca(long idPolitica);

	
	 /**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja cadastrar(Valida por Fornecedor, Concentração e Tipo)
	 * @param tipoCobranca
	 * @param idFornecedor
	 * @param diaDoMes
	 * @return Boolean
	 */
	boolean validarFormaCobrancaMensal(Long idPoliticaCobranca, Distribuidor distribuidor,TipoCobranca tipoCobranca,
			List<Long> idFornecedores, Integer diaDoMes);
	

	 /**
	 * Verifica se ja existe a Forma Cobranca Semanal que o usuário deseja cadastrar(Valida por Fornecedor, Concentração e Tipo)
	 * @param TipoCobranca
	 * @param idFornecedor
	 * @param domingo
	 * @param segunda
	 * @param terca
	 * @param quarta
	 * @param quinta
	 * @param sexta
	 * @param sabado
	 * @return Boolean
	 */
	boolean validarFormaCobrancaSemanal(Long idPoliticaCobranca, Distribuidor distribuidor,TipoCobranca tipoCobranca,
			List<Long> idFornecedores, Boolean domingo, Boolean segunda,
			Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta,
			Boolean sabado);
	
	List<TipoCobranca> obterTiposCobrancaDistribuidor();

}
