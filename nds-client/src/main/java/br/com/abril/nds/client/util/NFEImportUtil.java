package br.com.abril.nds.client.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;

import br.com.abril.nds.client.endereco.vo.ArquivoRetornoNFEVO;

/**
 * Classe utilitária que importa os arquivos de nota fiscal.
 * 
 * @author Discover Technology
 * 
 */
public class NFEImportUtil {

	/**
	 * Importa os arquivos que foram modificados na data parametrizada.
	 * 
	 * @param dataUltimaModificacao
	 */
	public void importArquivosModificadosEm(Calendar dataUltimaModificacao, final String PATH_NFE_IMPORTACAO) {

		File diretorio = new File("/teste");

		diretorio.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				
				Calendar lastModified = Calendar.getInstance();
				
				lastModified.setTimeInMillis(pathname.lastModified());
				
				return false;
			}
		});

	}
	
	public ArquivoRetornoNFEVO processarArquivoRetorno(File arquivo) {
		//TODO: processar arquivo
		return null;
	}
	
}
