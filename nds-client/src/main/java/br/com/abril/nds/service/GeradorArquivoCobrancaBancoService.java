package br.com.abril.nds.service;


/**
 * Interface que define serviços referentes 
 * a geração de arquivo de cobrança para o banco.
 * 
 * @author Discover Technology
 */
public interface GeradorArquivoCobrancaBancoService {
	

	/**
	 * Prepara os dados e gera o arquivo de cobrança para o banco.
	 */
	void prepararGerarArquivoCobrancaCnab();
	
}
