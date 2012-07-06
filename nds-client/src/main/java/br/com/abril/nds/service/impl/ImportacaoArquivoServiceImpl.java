package br.com.abril.nds.service.impl;

import java.io.File;

import org.springframework.stereotype.Service;

import br.com.abril.nds.factory.importacao.ImportacaoArquivoFactory;
import br.com.abril.nds.service.ImportacaoArquivoService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.strategy.importacao.ImportacaoArquivoStrategy;
import br.com.abril.nds.util.TipoImportacaoArquivo;

@Service
public class ImportacaoArquivoServiceImpl implements ImportacaoArquivoService{

	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo, TipoImportacaoArquivo tipoImportacaoArquivo) {
		
		ImportacaoArquivoStrategy importacaoArquivoStrategy = ImportacaoArquivoFactory.getStrategy(tipoImportacaoArquivo);
		
		RetornoImportacaoArquivoVO retornoImportacaoArquivoVO = importacaoArquivoStrategy.processarImportacaoArquivo(arquivo); 
		
		return retornoImportacaoArquivoVO;
	}
}
