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

	void gerarCobranca(Long idCota, Long idUsuario, Map<String, Boolean> mapNossoNumeroEnvioEmail)
		throws GerarCobrancaValidacaoException;
	
	void cancelarDividaCobranca(Set<Long> idMovimentoFinanceiroCota);

	void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota, Date dataOperacao);

	boolean verificarCobrancasGeradas(List<Long> idsCota);
	
	List<BoletoDistribuidor> gerarCobrancaBoletoDistribuidor(
			List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor, 
			TipoCobranca tipoCobranca, int semana);
	
	boolean verificarCobrancasGeradasNaDataVencimentoDebito(Date dataVencimentoDebito,Long... idsCota );

	void enviarDocumentosCobrancaEmail(String nossoNumero, String email)
			throws AutenticacaoEmailException;

	/**
	 * Envia Cobranças para email da Cota
	 * @param cota
	 * @param nossoNumeroEnvioEmail
	 * @throws AutenticacaoEmailException
	 */
	void enviarDocumentosCobrancaEmail(Cota cota,
									   Map<String, 
									   Boolean> nossoNumeroEnvioEmail) throws AutenticacaoEmailException;
	
	/**
	 * Gera cobranças para Cotas específicas
	 * @param cotas
	 * @param idUsuario
	 * @param enviaEmail
	 * @throws GerarCobrancaValidacaoException
	 */
	void gerarCobranca(List<Cota> cotas, 
	                   Long idUsuario,
	                   boolean enviaEmail) throws GerarCobrancaValidacaoException;
}