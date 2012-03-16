package br.com.abril.nds.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;

/**
 * Interface que define serviços referentes 
 * a leitura de arquivo de retorno do banco.
 * 
 * @author Discover Technology
 */
public interface LeitorRetornoBancoService {

	/**
	 * Obtém os pagamentos retornados pelo banco.
	 * 
	 * @param file - arquivo de retorno
	 * @param nomeArquivo - nome do arquivo
	 * 
	 * @return {@link ArquivoPagamentoBancoDTO}
	 */
	ArquivoPagamentoBancoDTO obterPagamentosBanco(File file,
			 									  String nomeArquivo) throws IOException,
			 									  							 ParseException;
	
}
