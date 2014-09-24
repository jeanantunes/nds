package br.com.abril.nds.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.ContratoVO;
import br.com.abril.nds.client.vo.FormaCobrancaDefaultVO;
import br.com.abril.nds.dto.ContratoTransporteDTO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
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
	    
	List<FormaCobrancaDefaultVO> obterFormaCobrancaCotaDefault(Integer numeroCota);
	
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
	* @param dataInicio
	* @param dataTermino
	* @return Array de bytes do contrato
	*/
	byte[] geraImpressaoContrato(Long idCota, Date dataInicio, Date dataTermino);
	
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
	* Salva o contrato da cota
	*
	* @param idCota
	* @param isRecebido
	* @param dataInicio
	* @param dataTermino
	*/
	void salvarContrato(Long idCota, boolean isRecebido, Date dataInicio, Date dataTermino);
	
	/**
	* obtém um arquivo de contrato anexo.
	*
	* @param idCota
	* @param tempDir diretorio temporario
	* @return ContratoVO quando o anexo for encontrato ou null quando o arquivo não existir
	*/
	public ContratoVO obterArquivoContratoRecebido(Long idCota, File tempDir);
	
	
	    /**
	* Recupera as informações de parametrização de cobrança do histórico de
	* titularidade da cota
	*
	* @param idCota
	* identificador da Cota
	* @param idHistorico
	* identificador do histórico de titularidade
	* @return {@link ParametroCobrancaCotaDTO} com as informações de parâmetros
	* de cobrança associados ao histórico de titularidade da cota
	*/
	ParametroCobrancaCotaDTO obterParametrosCobrancaHistoricoTitularidadeCota(Long idCota, Long idHistorico);
	
	    /**
	* Obtém as formas de cobrança do histórico de titularidade da cota
	*
	* @param idCota
	* identificador da Cota
	* @param idHistorico
	* identificador do histórico de titularidade
	* @return Lista de {@link FormaCobrancaDTO} com as informações de forma de
	* cobrança do histórico de titularidade da cota
	*/
	List<FormaCobrancaDTO> obterFormasCobrancaHistoricoTitularidadeCota(Long idCota, Long idHistorico);
	
	    /**
	* Obtém os fornecedores associados à forma de pagamento do histórico de
	* titularidade da cota
	*
	* @param idFormaPagto
	* identificador da forma de pagamento associado ao histórico de
	* titularidade da cota
	* @return fornecedores associados à forma de pagamento no histórico de
	* titularidade da cota
	*/
	List<FornecedorDTO> obterFornecedoresFormaPagamentoHistoricoTitularidade(Long idFormaPagto);
	
	    /**
	* Obtém as informações de forma de pagamento associadas ao histórico de
	* titularidade da cota
	*
	* @param idFormaPagto
	* identificador da forma de pagto associado ao histórico de
	* titularidade da cota
	* @return dto com as informações de forma de pagamento associados ao
	* histórico de titularidade da cota
	*/
	FormaCobrancaDTO obterFormaPagamentoHistoricoTitularidade(Long idFormaPagto);
	
	void alterarParametro(ParametroCobrancaCota parametroCobrancaCota);
	
	List<BigDecimal> comboValoresMinimos();
	
	void inserirFormaCobrancaDoDistribuidorNaCota(ParametroCobrancaCotaDTO parametroCobranca);

	/**
	 * Remove parametro de cobranca da cota e suas formas de cobranca
	 * @param formaCobrancaId
	 */
	void excluirParametroCobrancaCota(Long formaCobrancaId);

    void validarFormaCobranca(FormaCobrancaDTO formaCobranca);

    /**
     * Obtem Politica de Suspensao da Cota ou do Distribuidor
     * 
     * @param cota
     * @return PoliticaSuspensao
     */
	PoliticaSuspensao obterPoliticaSuspensaoCota(Cota cota);

	 /**
     * Verifica se cota utiliza politica de suspensao, seja propria ou do distribuidor
     * 
     * @param cota
     * @return boolean
     */
	boolean isSugereSuspensao(Cota cota);
}
