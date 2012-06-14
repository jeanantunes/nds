package br.com.abril.nds.service;

import java.util.Set;

public interface GerarCobrancaService {

	Set<String> gerarCobranca(Long idCota, Long idUsuario, boolean indValidaConclusaoOperacaoConferencia);
	
	Boolean validarDividaGeradaDataOperacao();
	
	void cancelarDividaCobranca(Set<Long> idMovimentoFinanceiroCota);

	void cancelarDividaCobranca(Long idMovimentoFinanceiroCota);
}