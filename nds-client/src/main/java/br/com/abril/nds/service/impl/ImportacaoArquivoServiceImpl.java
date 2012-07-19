package br.com.abril.nds.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.service.ImportacaoArquivoService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.strategy.importacao.ImportacaoArquivoStrategy;
import br.com.abril.nds.util.TipoImportacaoArquivo;

@Service
public class ImportacaoArquivoServiceImpl implements ImportacaoArquivoService{

	@Autowired
	private ImportacaoArquivoStrategy importacaoDeArquivoMatrizStrategy;
	
	@Autowired
	private ImportacaoArquivoStrategy importacaoDeArquivoHistoricoVendaStrategy;
	
	@Autowired
	private ImportacaoArquivoStrategy importacaoDeArquivoProdutoStrategy;
	
	@Override
	@Transactional
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo, TipoImportacaoArquivo tipoImportacaoArquivo) {
		
		ImportacaoArquivoStrategy importacaoArquivoStrategy = getStrategy(tipoImportacaoArquivo);
		
		RetornoImportacaoArquivoVO retornoImportacaoArquivoVO = importacaoArquivoStrategy.processarImportacaoArquivo(arquivo); 
		
		return retornoImportacaoArquivoVO;
	}
	
	
	 /* Obtém a estratégia de importação de arquivos de acordo com o tipo.
	 * 
	 * @param tipoImportacaoArquivo - tipo de importação de arquivo
	 * 
	 * @return {@link ImportacaoArquivoStrategy}
	 */
	private ImportacaoArquivoStrategy getStrategy(TipoImportacaoArquivo tipoImportacaoArquivo) {
		
		ImportacaoArquivoStrategy importacaoArquivoStrategy = null;

		switch (tipoImportacaoArquivo) {
		
			case HISTORICO_VENDA:
				
				importacaoArquivoStrategy = importacaoDeArquivoHistoricoVendaStrategy;
				
				break;
				
			case MATRIZ:
				
				importacaoArquivoStrategy = importacaoDeArquivoMatrizStrategy;
				
				break;
	
			case PRODUTO:
				
				importacaoArquivoStrategy = importacaoDeArquivoProdutoStrategy;
				
				break;
				
			default:
				
				throw new UnsupportedOperationException("Tipo de importação de arquivo não suportado!");
		}
		
		return importacaoArquivoStrategy;
	}
}
