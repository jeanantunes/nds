package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.vo.RetornoImportacaoArquivoVO;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 *
 * Classe que abstrai a leitura e geração de log dos arquivos importados
 *
 * @author Discover Technology
 *
 */
public abstract class ImportacaoAbstractStrategy<T> {

	private static final Logger logger = Logger.getLogger(ImportacaoAbstractStrategy.class);

	protected static final String MENSAGEM_ERRO_PARSE_DADOS = "Parse das informações contidas na linha do arquivo inválida!";

	protected static final String MENSAGEM_ERRO_FORMATO_DADOS="Formato das informações contidas na linha do arquivo inválida!";

	protected Date dataCriacaoArquivo;

	/**
	 * Efetua o processamento do dados referente ao arquivo
	 *
	 * @param input -input de dados referente a leitura da linha do arquivo
	 */	
	protected abstract void processarDados(Object input);


	
	
	protected RetornoImportacaoArquivoVO processarArquivo(File arquivo, Class<T> clazz){
	// simple filtering of properties (build, description)
		dataCriacaoArquivo = new Date(arquivo.lastModified());
				
		CsvMapper mapper = new CsvMapper();		
		CsvSchema schema = mapper
				.schemaFor(clazz)
				.withColumnSeparator(';');
		
		  
		MappingIterator<Entry> it;
		try {
			it = mapper
				    .reader(clazz)
				    .with(schema)				    
				    .readValues(arquivo);
		
			int linhaArquivo = 0;
		
			while (it.hasNextValue()) {
				linhaArquivo++;
				try {
					
					Object input = it.nextValue();
					processarDados((T)input);

				} catch (ImportacaoException e) {

					RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO(
							new String[]{
									e.getMessage()}
									, linhaArquivo
									, it.toString()
									, false
								);
					logger.error(retorno.toString());
					//return retorno;
				}
			}
		} catch (IOException ex) {
			logger.fatal("Erro na leitura de arquivo", ex);
			throw new ImportacaoException(ex.getMessage());
		} 		

		return new RetornoImportacaoArquivoVO(true) ;
	}
}