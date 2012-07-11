package br.com.abril.nds.service;

import java.io.File;

import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.util.TipoImportacaoArquivo;

public interface ImportacaoArquivoService  {

	/**
	 * Efetua o processamento da importação do arquivo.
	 * 
	 * @param arquivo -  arquivo a ser importado
	 * @param tipoImportacaoArquivo - tipo de importação de arquivo
	 * 
	 * @return RetornoImportacaoArquivoVO
	 */
	RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo, TipoImportacaoArquivo tipoImportacaoArquivo);
}
