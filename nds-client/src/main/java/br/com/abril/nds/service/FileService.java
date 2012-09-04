package br.com.abril.nds.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import br.com.abril.nds.dto.ArquivoDTO;

public interface FileService {

	/**
	 * Preenche pasta tempo com arquivo da pasta Fixa
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * 
	 */
	void resetTemp(String dirEspecifico ) throws FileNotFoundException, IOException;
		
		
	/**
	 * Persiste arquivo temporario no diretório fixo
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 
	 */
	void persistirTemporario(String dirEspecifico) throws FileNotFoundException, IOException;

	/**
	 * Altera arquivo temporário
	 */
	void setArquivoTemp(String dirBase, String nomeArquivo,
			InputStream inputStream) throws IOException;

	/**
	 * Esvazia diretorio temporario
	 * 
	 */
	void esvaziarTemp(String dirBase);

	/**
	 * Obtém arquivo temporário
	 * 
	 * @param dirEspecifico
	 * @param pathTipoArquivo
	 * @throws IOException 
	 */
	ArquivoDTO obterArquivoTemp(String dirBase) throws FileNotFoundException, IOException;


	/**
	 * Obtém nome do arquivo temporario salvo
	 */
	String obterNomeArquivoTemp(String dirBase);
}
