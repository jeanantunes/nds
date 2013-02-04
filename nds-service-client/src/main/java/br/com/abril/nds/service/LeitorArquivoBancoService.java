package br.com.abril.nds.service;

import java.io.File;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;

/**
 * Interface que define serviços referentes 
 * a leitura de arquivo de retorno do banco.
 * 
 * @author Discover Technology
 */
public interface LeitorArquivoBancoService {

	/**
	 * Obtém os pagamentos retornados pelo banco.
	 * 
	 * @param file - arquivo de retorno
	 * @param nomeArquivo - nome do arquivo
	 * 
	 * @return {@link ArquivoPagamentoBancoDTO}
	 */
	ArquivoPagamentoBancoDTO obterPagamentosBanco(File file,
			 									  String nomeArquivo);
	
}
