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
	
}
