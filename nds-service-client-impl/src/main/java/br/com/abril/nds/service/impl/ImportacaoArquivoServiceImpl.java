package br.com.abril.nds.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.service.ImportacaoArquivoService;
import br.com.abril.nds.strategy.importacao.ImportacaoArquivoStrategy;
import br.com.abril.nds.util.TipoImportacaoArquivo;
import br.com.abril.nds.vo.RetornoImportacaoArquivoVO;

@Service
public class ImportacaoArquivoServiceImpl implements ImportacaoArquivoService{

	@Autowired
	private ImportacaoArquivoStrategy importacaoDeArquivoHistoricoVendaStrategy;

	@Autowired
	private ImportacaoArquivoStrategy importacaoDeArquivoHistoricoFinanceiroStrategy;	
		
	@Override
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
				
			case HISTORICO_FINANCEIRO:
				
				importacaoArquivoStrategy = importacaoDeArquivoHistoricoFinanceiroStrategy;
				
				break;
				
			default:
				
				throw new UnsupportedOperationException("Tipo de importação de arquivo não suportado!");
		}
		
		return importacaoArquivoStrategy;
	}
}
