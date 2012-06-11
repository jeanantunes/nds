package br.com.abril.nds.client.util;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.endereco.vo.ArquivoRetornoNFEVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.util.ArquivoImportFileFilter;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;

/**
 * Classe utilitária que importa os arquivos de nota fiscal.
 * 
 * @author Discover Technology
 * 
 */
public class NFEImportUtil {
	
	
	/**
	 * Importa os arquivos de nota gerados pelo emissor respeitando os parametros.
	 * 
	 * @param dataUltimaModificacao data de modificação ou criação do arquivo
	 * @param pathNFEImportacao path dos arquivos de nota gerado pelo emissor
	 */
	public static List<File> importArquivosModificadosEm(Date dataUltimaModificacao, String pathNFEImportacao) {

		File diretorio = new File(pathNFEImportacao);
		
		if (!diretorio.isDirectory()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O diretório parametrizado para geração de notas está incorreto"));
		}
	
		ArquivoImportFileFilter fileFilter = new ArquivoImportFileFilter(dataUltimaModificacao, FileType.XML);
				
		return Arrays.asList(diretorio.listFiles(fileFilter));
	}
	
	public ArquivoRetornoNFEVO processarArquivoRetorno(File arquivo) {
		//TODO: processar arquivo
		return null;
	}
	
}
