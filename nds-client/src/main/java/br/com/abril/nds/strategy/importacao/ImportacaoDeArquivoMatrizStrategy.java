package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;

/**
 * Estratégia de importação de arquivos referente a Matriz de recolhimento e lançamento.
 * 
 * @author Discover Technology
 *
 */
public class ImportacaoDeArquivoMatrizStrategy implements ImportacaoArquivoStrategy {
	
	@Autowired
	private FixedFormatManager ffm;
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		// FIXME implementar a importação de arquivo referente a Matriz
			
		return null;
	}
	
}
