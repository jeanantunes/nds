package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.MappingIterator;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ImportacaoException;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;

/**
 *
 * Classe que abstrai a leitura e geração de log dos arquivos importados
 *
 * @author Discover Technology
 *
 */
public abstract class ImportacaoAbstractStrategy {

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

	/**
	 *
	 * Efetua o parse da linha do arquivo em um objeto
	 *
	 * @param linha - linha do arquivo
	 * @return Object
	 */
	protected abstract Object parseDados(String linha);

	/**
	 * Efetua o processamento do arquivo informado
	 *
	 * @param arquivo - arquivo a ser importado
	 * @return RetornoImportacaoArquivoVO
	 */
	protected RetornoImportacaoArquivoVO processarArquivo(File arquivo){

		dataCriacaoArquivo = new Date(arquivo.lastModified());

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

				Object  input = parseDados(linha);

				processarDados(input);

			} catch (ImportacaoException e) {

				RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO(new String[]{e.getMessage()},linhaArquivo,linha,false);
				logger.error(retorno.toString());
				//return retorno;
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
	
	
	protected RetornoImportacaoArquivoVO processarArquivo2(File arquivo){
	// simple filtering of properties (build, description)
		dataCriacaoArquivo = new Date(arquivo.lastModified());

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

				Object  input = parseDados(linha);

				processarDados(input);

			} catch (ImportacaoException e) {

				RetornoImportacaoArquivoVO retorno = new RetornoImportacaoArquivoVO(new String[]{e.getMessage()},linhaArquivo,linha,false);
				logger.error(retorno.toString());
				//return retorno;
			}
		}

		try {
			in.close();
		} catch (IOException e) {
			logger.fatal("Erro na leitura de arquivo", e);
			throw new ImportacaoException(e.getMessage());
		}

		return new RetornoImportacaoArquivoVO(true) ;
		/*
		CsvMapper mapper = new CsvMapper();
			MappingIterator<Entry> it = mapper
				    .reader(User.class)
				    .with(schema)
				    .readValues(new File("Users.csv"());
				  List<User> users = new ArrayList<User>();
				  while (it.hasNextValue()) {
				    User user = it.nextValue();
				    // do something?
				    list.add(user);
				  }*/
	}
}
