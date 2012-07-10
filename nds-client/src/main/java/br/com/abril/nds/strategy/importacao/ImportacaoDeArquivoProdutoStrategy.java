package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.springframework.stereotype.Component;

import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;

/**
 * Estratégia de importação de arquivos referente a Produtos.
 * 
 * @author Discover Technology
 *
 */
@Component("importacaoDeArquivoProdutoStrategy")
public class ImportacaoDeArquivoProdutoStrategy implements ImportacaoArquivoStrategy {

	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		// FIXME imprementar a importação referente a Produtos
		return null;
	}
}
