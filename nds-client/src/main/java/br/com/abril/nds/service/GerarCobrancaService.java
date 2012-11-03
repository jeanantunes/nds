package br.com.abril.nds.service;

import java.io.IOException;
import java.util.Set;

import br.com.abril.nds.exception.GerarCobrancaValidacaoException;

public interface GerarCobrancaService {

	void gerarCobranca(Long idCota, Long idUsuario, Set<String> setNossoNumero)
		throws GerarCobrancaValidacaoException, IOException;
	
	void gerarCobrancaCota(Long idCota, Long idUsuario, Set<String> setNossoNumero)
			throws GerarCobrancaValidacaoException;
	
	Boolean validarDividaGeradaDataOperacao();
	
	void cancelarDividaCobranca(Set<Long> idMovimentoFinanceiroCota);

	void cancelarDividaCobranca(Long idMovimentoFinanceiroCota);
	
	void gerarCobrancaFornecedor(Long idFornecedor, Long idUsuario, Set<String> setNossoNumero) throws GerarCobrancaValidacaoException;
}