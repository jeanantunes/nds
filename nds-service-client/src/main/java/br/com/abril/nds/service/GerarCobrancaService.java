package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;

public interface GerarCobrancaService {

	void gerarCobranca(Long idCota, Long idUsuario, Map<String, Boolean> mapNossoNumeroEnvioEmail)
		throws GerarCobrancaValidacaoException;
	
	void cancelarDividaCobranca(Set<Long> idMovimentoFinanceiroCota);

	void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota);

	boolean verificarCobrancasGeradas(List<Long> idsCota);
	
	List<BoletoDistribuidor> gerarCobrancaBoletoDistribuidor(
			List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor, 
			TipoCobranca tipoCobranca, int semana);
	
	boolean verificarCobrancasGeradasNaDataVencimentoDebito(Date dataVencimentoDebito,Long... idsCota );

	void enviarDocumentosCobrancaEmail(String nossoNumero, String email)
			throws AutenticacaoEmailException;
}