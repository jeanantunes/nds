package br.com.abril.nds.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import br.com.abril.nds.dto.ArquivoDTO;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

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
	 * @param dirBase
	 * Elimina o diretorio "real" dentro do diretorio-base
	 */
	void eliminarReal(String dirBase);
	
	/**
	 * Obtém arquivo temporário
	 * 
	 * @param dirEspecifico
	 * @param pathTipoArquivo
	 * @throws IOException 
	 */
	ArquivoDTO obterArquivoTemp(String dirBase) throws FileNotFoundException, IOException;

	
	/**
	 * Remove todos os arquivos do diretório parametrizado
	 */
	void limparDiretorio(File diretorio);

	/**
	 * Obtém nome do arquivo temporario salvo
	 */
	String obterNomeArquivoTemp(String dirBase);
	
	/**
	 * Valida o tamanho e o tipo do arquivo de acordo com os parametros.
	 * Quando arquivo for inválido lança uma ValidacaoException
	 * 
	 * @param maxSize - tamanho maximo em Megabytes (MB)
	 * @param extensoes - lista de extensões permitidas
	 * @param file - arquivo
	 * 
	 * @throws IOException 
	 * 
	 */
	void validarArquivo(int maxSize, UploadedFile file, FileType... extensoes) throws IOException;
	
	/**
	 * faz upload de arquivo em um diretorio temporario
	 * 
	 * @param file
	 * 
	 * @return String - filePath
	 */
	String uploadTempFile(UploadedFile file, String tempPath);
}
