package br.com.abril.nds.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.export.FileExporter.FileType;

public class FileImportUtil {
	
	/**
	 * Utilitario para importar arquivos respeitando os parametros.
	 * 
	 * @param pathImportacao diretorio de importação
	 * @param dataUltimaModificacao data de modificação ou criação do arquivo
	 * @param fileType tipo do arquivo que será importado
	 * 
	 * @throws FileNotFoundException caso o path para o diretório seja inválido
	 * 
	 * @return lista de arquivos dentro do diretório
	 */
	public static List<File> importArquivosModificadosEm(String pathImportacao, Date dataUltimaModificacao,  FileType fileType) throws FileNotFoundException {

		File diretorio = new File(pathImportacao);
		
		if (!diretorio.isDirectory()) {
			throw new FileNotFoundException("Diretório inválido");
		}
		
		ArquivoImportFileFilter fileFilter = new ArquivoImportFileFilter(dataUltimaModificacao, fileType);
						
		return Arrays.asList(diretorio.listFiles(fileFilter));
	}
	
	
	/**
	 * Obtem a extensão do arquivo pelo nome.
	 * 
	 * @param fileName nome do arquivo
	 * @return extensão do arquivo no formato ".ext"
	 */
	public static String getExtensionFile(String fileName) {
		
		String extension = null;
		
		extension = fileName.substring(fileName.lastIndexOf("."));
		
		return extension;
	}
	
	/**
	 * Obtem o content-type de um arquivo pela sua extensão.
	 * 
	 * Ex: getContentTypeByExtension(".jpeg")
	 *     return "image/jpeg"
	 * 
	 * @param extension
	 * @return content-type
	 */
	public static String getContentTypeByExtension(String extension) {
		
		if (StringUtil.isEmpty(extension)) return null;
		
		String contentType = null;
		
		if(!extension.startsWith(".")){
			extension = "."+extension;
		}
		
		FileType[] fileTypes = FileType.values();
		
		for (FileType fileType : fileTypes) {
			
			if(extension.equals(fileType.getExtension())){
				contentType = fileType.getContentType();
				break;
			}
		}
		
		return contentType;
	}
}
