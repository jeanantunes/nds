package br.com.abril.nds.factory.importacao;

import br.com.abril.nds.strategy.importacao.ImportacaoArquivoStrategy;
import br.com.abril.nds.strategy.importacao.ImportacaoDeArquivoHistoricoVendaStrategy;
import br.com.abril.nds.strategy.importacao.ImportacaoDeArquivoMatrizStrategy;
import br.com.abril.nds.strategy.importacao.ImportacaoDeArquivoProdutoStrategy;
import br.com.abril.nds.util.TipoImportacaoArquivo;

/**
 *  Fábrica de estratégias de importação de arquivos
 *  
 *  @author Discover Technology
 *
 */
public class ImportacaoArquivoFactory {
	
	/**
	 * Obtém a estratégia de importação de arquivos de acordo com o tipo.
	 * 
	 * @param tipoImportacaoArquivo - tipo de importação de arquivo
	 * 
	 * @return {@link ImportacaoArquivoStrategy}
	 */
	public static ImportacaoArquivoStrategy getStrategy(TipoImportacaoArquivo tipoImportacaoArquivo) {
		
		ImportacaoArquivoStrategy importacaoArquivoStrategy = null;

		switch (tipoImportacaoArquivo) {
		
			case HISTORICO_VENDA:
				
				importacaoArquivoStrategy = new ImportacaoDeArquivoHistoricoVendaStrategy();
				
				break;
				
			case MATRIZ:
				
				importacaoArquivoStrategy = new ImportacaoDeArquivoMatrizStrategy();
				
				break;
	
			case PRODUTO:
				
				importacaoArquivoStrategy = new ImportacaoDeArquivoProdutoStrategy();
				
				break;
				
			default:
				
				throw new UnsupportedOperationException("Tipo de importação de arquivo não suportado!");
		}
		
		return importacaoArquivoStrategy;
	}
}
