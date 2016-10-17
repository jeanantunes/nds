package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.client.vo.baixaboleto.TipoEmissaoDocumento;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;

public interface GerarCobrancaService {
	
	void cancelarDividaCobranca(Set<Long> idMovimentoFinanceiroCota, boolean excluiFinanceiro);

	void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota, Date dataOperacao, boolean excluiFinanceiro);

	boolean verificarCobrancasGeradas(List<Long> idsCota);
	
	List<BoletoDistribuidor> gerarCobrancaBoletoDistribuidor(
			List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor, 
			TipoCobranca tipoCobranca, int semana);
	
	boolean verificarCobrancasGeradasNaDataVencimentoDebito(Date dataVencimentoDebito,Long... idsCota );

	/**
	 * Envia Cobranças para email da Cota
	 * 
	 * @param cota
	 * @param nossoNumeroEnvioEmail
	 */
	void enviarDocumentosCobrancaEmail(Cota cota, Set<String> nossoNumeroEnvioEmail);
	
	/**
	 * Gera cobranças para Cotas específicas
	 * 
	 * @param cotas
	 * @param idUsuario
	 * @param enviaEmail
	 * @throws GerarCobrancaValidacaoException
	 */
	void gerarCobranca(List<Cota> cotas, 
	                   Long idUsuario,
	                   boolean enviaEmail) throws GerarCobrancaValidacaoException;
	
	/**
	 * Consolida Financeiro, Gera Divida e Gera Cobrança
	 * 
	 * @param idCota
	 * @param idUsuario
	 * @param setNossoNumeroEnvioEmail
	 * @param setNossoNumeroCentralizacao
	 * @throws GerarCobrancaValidacaoException
	 */
	void gerarCobranca(Long idCota, 
			           Long idUsuario, 
			           Set<String> setNossoNumeroEnvioEmail,
			           Set<String> setNossoNumeroCentralizacao) throws GerarCobrancaValidacaoException;
	
	/**
	 * Consolida Financeiro, Gera Divida e Gera Cobrança para cotas de Tipos Específicos (A_VISTA/CONSIGNADO)
	 * 
	 * @param idCota
	 * @param idUsuario
	 * @param setNossoNumeroEnvioEmail
	 * @param setNossoNumeroCentralizacao
	 * @param tiposCota
	 * @throws GerarCobrancaValidacaoException
	 */
	void gerarCobranca(Long idCota, 
			           Long idUsuario, 
			           Set<String> setNossoNumero,
			           Set<String> setNossoNumeroCentralizacao, 
			           List<TipoCota> tiposCota) throws GerarCobrancaValidacaoException;
	
	/**
	 * Consolida Financeiro, Gera Divida e Posterga Divida Gerada para Cotas especificas
	 * 
	 * @param List<Cota>
	 * @param idUsuario
	 * @throws GerarCobrancaValidacaoException
	 */
	void gerarDividaPostergadaCotas(List<Cota> cotas, 
			                        Long idUsuario)
			throws GerarCobrancaValidacaoException;

	/**
	 * Consolida Financeiro, Gera Divida e Posterga Divida Gerada para Cota especifica
	 * 
	 * @param idCota
	 * @param idUsuario
	 * @throws GerarCobrancaValidacaoException
	 */
	void gerarDividaPostergada(Long idCota, 
			                   Long idUsuario) throws GerarCobrancaValidacaoException;
	
	/**
	 * Realiza todas as validações necessárias para certificar que é possível realizar o envio dos documentos por email.
	 * 
	 * @param nossoNumero
	 * @param idCota
	 * @return boolean
	 */
	boolean aceitaEnvioEmail(Cota cota, String nossoNumero);

	boolean aceitaEmissaoDocumento(Cota cota, TipoEmissaoDocumento tipoEmissaoDocumento);

    boolean aceitaEmissaoDocumento(Long idCota, TipoEmissaoDocumento tipoEmissaoDocumento);
	
	/**
    * Obtem Data de Vencimento onforme Parametros 
    * @param dataConsolidado
	 * @param fatorVencimento
	 * @param localidade TODO
    * @return Date
    */
	Date obterDataVencimentoCobrancaCota(Date dataConsolidado, Integer fatorVencimento, String localidade);
	
	void gerarCobrancaBoletoAvulso(Long idUsuario, MovimentoFinanceiroCota movimentoFinanceiroCota, Map<Integer, Long> cotasBanco) throws GerarCobrancaValidacaoException;
	
	void gerarCobrancaBoletoNFe(Long idUsuario, MovimentoFinanceiroCota movimentoFinanceiroCota, Set<String> setNossoNumero) throws GerarCobrancaValidacaoException;
}