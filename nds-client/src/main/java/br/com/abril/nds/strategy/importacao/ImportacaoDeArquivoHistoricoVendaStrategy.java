package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.integracao.ems0108.inbound.EMS0108Input;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.strategy.importacao.input.HistoricoVenda;

/**
 * Estratégia de importação de arquivos referente a Histórico de Vendas
 * 
 * @author Discover Technology
 *
 */
@Component("importacaoDeArquivoHistoricoVendaStrategy")
public class ImportacaoDeArquivoHistoricoVendaStrategy implements ImportacaoArquivoStrategy {

	private static final Logger logger = Logger.getLogger(ImportacaoDeArquivoHistoricoVendaStrategy.class);
	
	@Override
	public RetornoImportacaoArquivoVO processarImportacaoArquivo(File arquivo) {
		 
		FileReader in = null;
		try {
			in = new FileReader(arquivo);
		} catch (FileNotFoundException ex) {
			logger.fatal("Erro na leitura de arquivo", ex);
			throw new ImportacaoException(ex.getMessage());
		}
		
		Scanner scanner = new Scanner(in);
		int linhaArquivo = 0;

		while (scanner.hasNextLine()) {

			String linha = scanner.nextLine();
			linhaArquivo++;
			
			// Ignora linha vazia e aquele caracter estranho em formato de seta para direita
			if (StringUtils.isEmpty(linha) ||  ((int) linha.charAt(0)  == 26) ) {
				continue;
			} 
			
			try {
				
				HistoricoVenda  input = parseDados(linha);
				
				processarDados(input);
				
			} catch (ImportacaoException e) {
				
				RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO(new String[]{e.getMessage()},linhaArquivo,linha,false);
				logger.error(retorno.toString());
				return retorno; 
			}
		}
		
		try {
			in.close();
		} catch (IOException e) {
			logger.fatal("Erro na leitura de arquivo", e);
			throw new ImportacaoException(e.getMessage());
		}
		
		return new RetornoImportacaoArquivoVO(true) ;
	}
	
	private HistoricoVenda parseDados(String linha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processarImportacaoDados(Object input){}
	
	
	private void processarDados(HistoricoVenda historicoVenda){
		
	}

}
