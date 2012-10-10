package br.com.abril.nds.service;

import java.io.IOException;

/**
 * Interface que define serviços referentes 
 * a geração de arquivo de cobrança para o banco.
 * 
 * @author Discover Technology
 */
public interface GeradorArquivoCobrancaBancoService {
	

	/**
	 * Prepara os dados e gera o arquivo de cobrança para o banco. 
	 * 
	 * @throws IOException
	 */
	void prepararGerarArquivoCobrancaCnab() throws IOException;
	
}
