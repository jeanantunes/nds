package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.apache.poi.ss.formula.functions.T;

import com.fasterxml.jackson.core.type.TypeReference;

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
	
}
