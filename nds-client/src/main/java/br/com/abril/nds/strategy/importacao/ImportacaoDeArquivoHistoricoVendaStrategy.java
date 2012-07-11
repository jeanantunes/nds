package br.com.abril.nds.strategy.importacao;

import java.io.File;

import org.springframework.stereotype.Component;

import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;

/**
 * Estratégia de importação de arquivos referente a Histórico de Vendas
 * 
 * @author Discover Technology
 *
 */
@Component("importacaoDeArquivoHistoricoVendaStrategy")
public class ImportacaoDeArquivoHistoricoVendaStrategy implements ImportacaoArquivoStrategy {

	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		 
		//FIXME implementar a logica de importação referente a Historico de Vendas
		return null;
	}
	
	@Override
	public void processarImportacaoDados(Object input){}

}
