package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.integracao.DistribuidorService;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(ImportacaoAbstractStrategy.class);
    private static final Marker FATAL = MarkerFactory.getMarker("FATAL");

    protected static final String MENSAGEM_ERRO_PARSE_DADOS = "Parse das informações contidas na linha do arquivo inválida!";

    protected static final String MENSAGEM_ERRO_FORMATO_DADOS = "Formato das informações contidas na linha do arquivo inválida!";

	protected Date dataCriacaoArquivo;
	
	@Autowired
	private DistribuidorService distribuidorService;

	/**
	 * Efetua o processamento do dados referente ao arquivo
	 *
	 * @param input -input de dados referente a leitura da linha do arquivo
	 * @param dataOperacao 
	 */	
	protected abstract void processarDados(Object input, Date dataOperacao);


	
	
	protected RetornoImportacaoArquivoVO processarArquivo(File arquivo, Class<T> clazz){
	// simple filtering of properties (build, description)
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();  
		
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
					processarDados((T)input, dataOperacao);

				} catch (ImportacaoException e) {

					RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO(
							new String[]{
									e.getMessage()}
									, linhaArquivo
									, it.toString()
									, false
								);
					LOGGER.error(retorno.toString());
					//return retorno;
					
					// Tratamento de qualquer outra exception
				}  catch (ValidacaoException e) {
					RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO(
							new String[]{
									e.getMessage()}
									, linhaArquivo
									, it.toString()
									, false
								);
					LOGGER.error(retorno.toString());
				}
			}
		} catch (IOException ex) {
            LOGGER.error(FATAL, "Erro na leitura de arquivo", ex);
			throw new ImportacaoException(ex.getMessage());
		} 		

		return new RetornoImportacaoArquivoVO(true) ;
	}
}
