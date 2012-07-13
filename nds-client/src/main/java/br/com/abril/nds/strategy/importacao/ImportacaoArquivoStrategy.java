package br.com.abril.nds.strategy.importacao;

import java.io.File;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;

public interface ImportacaoArquivoStrategy {
	
	/**
	 * Efetua o processamento da importação do arquivo.
	 * 
	 * @param arquivo -  arquivo a ser importado
	 * 
	 * @return RetornoImportacaoArquivoVO
	 */
	RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo);
	
	/**
	 * Efetua o processamento da importação do arquivo.
	 * 
	 * @param input - Objeto com os input para importação dos dados
	 *  
	 * @throws  ImportacaoException - lança exceptio caso ocorra algum erro de logica de inserção e alteração
	 */
	void processarImportacaoDados(Object input) throws ImportacaoException;
}
