package br.com.abril.nds.service;

import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dto.ContratoTransporteDTO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.TipoCobranca;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
public interface ParametroCobrancaCotaService {

    /**
     * Obtém parametros de Cobranca da cota
     * @param idCota
     * @return Data Transfer Object com parametros de Cobranca da cota 
     */
    ParametroCobrancaCotaDTO obterDadosParametroCobrancaPorCota(Long idCota);
    
    /**
	 * Método responsável por obter bancos relacionados ao tipo de cobrança para preencher combo da camada view
	 * @param Tipo de cobrança
	 * @return comboBancos: bancos cadastrados relacionados ao tipo de cobrança
	 */
    List<ItemDTO<Integer, String>> getComboBancosTipoCobranca(TipoCobranca tipoCobranca);
    
    /**
	 * Método responsável por obter formas de cobrança para preencher combo da camada view
	 * @return comboTiposPagamento: formas de cobrança cadastrados
	 */
    List<ItemDTO<TipoCobranca, String>> getComboTiposCobranca();
    
    /**
   	 * Método responsável por obter combo de fornecedores relacionados com a Cota
   	 * @param idCota
   	 * @return comboFornecedores: fornecedores relacionados com a Cota
   	 */
    List<ItemDTO<Long, String>> getComboFornecedoresCota(Long idCota);
    
    /**
     * Posta parametros de Cobranca da cota
     * @param Data Transfer Object com parametros de Cobranca da cota 
     */
    void postarParametroCobranca(ParametroCobrancaCotaDTO cotaCobranca);
    
    /**
     * Posta forma de Cobranca
     * @param Data Transfer Object com forma de Cobranca
     */
    void postarFormaCobranca(FormaCobrancaDTO formaCobranca);

    /**
     * Obtém Formas de Cobrança da cota
     * @param idCota: ID da cota
     * @return Formas de cobrança da Cota
     */
    List<FormaCobrancaDTO> obterDadosFormasCobrancaPorCota(Long idCota);
    
    /**
     * Obtém Forma de Cobrança do Parametro de Cobranca
     * @param idParametro: ID do parametro de cobranca
     * @return Forma de cobrança do parametro de cobranca
     */
    FormaCobrancaDTO obterDadosFormaCobranca(Long idForma);

    
    /**
     * Obtem os dados para o contrato de prestação de serviços de transportes de revistas.<br/>
     * 
     * A composição do contrato de prestação de serviços as informações serão obtidas:
     * <ul>
	 * <li>Contratante com base nos dados da distribuidora.</li>
 	 * <li>Contratada com base nos dados da cota parametrizada.</li>
  	 * <li>Condições da contratação(Prazo, Inicio e Termino e Aviso Prévio para Recisão) obtido nos parâmetros da distribuidora.</li>
	 * </ul>
     * @param idCota Id da cota
     * @return
     */
	public abstract ContratoTransporteDTO obtemContratoTransporte(long idCota);
	
	
	/**
	 * Obtém o contrato em forma de array de bytes
	 * @param idCota
	 * @return Array de bytes do contrato
	 */
	byte[] geraImpressaoContrato(Long idCota);
	
	
	/**
	 * Obtém a forma de cobranca principal da cota
	 * @param idCota
	 * @return FormaCobrança principal da cota
	 */
	 FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota);
	 
	 
	 /**
	 * Obtém lista de forma de cobranca da Cota
	 * @param ID da Cota
	 * @return {@link List<FormaCobranca>}
	 */
	 List<FormaCobranca> obterFormasCobrancaCota(Long idCota);
		 
		 	 
	 /**
	 * Obtém quantidade de forma de cobranca da Cota
	 * @param ID da Cota
	 * @return Quantidade de formas de cobrança para a Cota
	 */
	 int obterQuantidadeFormasCobrancaCota(Long idCota);
	 
	 
	 /**
	 * Exclusão de Forma de Cobranca
	 * @param ID da Forma de Cobrança
	 */
	 void excluirFormaCobranca(Long idFormaCobranca);
	 
	 
	 /**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja cadastrar(Valida por Fornecedor, Concentração e Tipo)
	 * @param tipoCobranca
	 * @param idFornecedor
	 * @param diaDoMes
	 * @return Boolean
	 */
	 boolean validarFormaCobrancaMensal(Long idCota, TipoCobranca tipoCobranca, List<Long> idFornecedores, Integer diaDoMes);
		 
	 
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
	 boolean validarFormaCobrancaSemanal(Long idCota, TipoCobranca tipoCobranca, List<Long> idFornecedores, Boolean domingo, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado);
		 	 
	 
}
