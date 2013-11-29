package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;

public interface GerarCobrancaService {
	
	void cancelarDividaCobranca(Set<Long> idMovimentoFinanceiroCota, boolean excluiFinanceiro);

	void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota, Date dataOperacao, boolean excluiFinanceiro);

	boolean verificarCobrancasGeradas(List<Long> idsCota);
	
	List<BoletoDistribuidor> gerarCobrancaBoletoDistribuidor(
			List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor, 
			TipoCobranca tipoCobranca, int semana);
	
	boolean verificarCobrancasGeradasNaDataVencimentoDebito(Date dataVencimentoDebito,Long... idsCota );

	void enviarDocumentosCobrancaEmail(String nossoNumero, String email)
			throws AutenticacaoEmailException;

	/**
	 * Envia Cobranças para email da Cota
	 * 
	 * @param cota
	 * @param nossoNumeroEnvioEmail
	 * @throws AutenticacaoEmailException
	 */
	void enviarDocumentosCobrancaEmail(Cota cota,
									   Map<String, 
									   Boolean> nossoNumeroEnvioEmail) throws AutenticacaoEmailException;
	
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
	 * @param setNossoNumero
	 * @throws GerarCobrancaValidacaoException
	 */
	void gerarCobranca(Long idCota, 
			           Long idUsuario, 
			           Map<String, Boolean> mapNossoNumeroEnvioEmail) throws GerarCobrancaValidacaoException;
	
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
    * Obtem Data de Vencimento onforme Parametros 
    * @param dataConsolidado
    * @param fatorVencimento
    * @return Date
    */
	Date obterDataVencimentoCobrancaCota(Date dataConsolidado, Integer fatorVencimento);
}