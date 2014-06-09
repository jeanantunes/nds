package br.com.abril.nds.strategy.importacao;

import java.io.File;

import br.com.abril.nds.vo.RetornoImportacaoArquivoVO;

public interface ImportacaoArquivoStrategy {
	
	/**
	 * Efetua o processamento da importação do arquivo.
	 * 
	 * @param arquivo -  arquivo a ser importado
	 * 
	 * @return RetornoImportacaoArquivoVO
	 */
	RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo);
	
}
