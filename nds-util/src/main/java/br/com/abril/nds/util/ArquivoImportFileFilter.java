package br.com.abril.nds.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

import br.com.abril.nds.util.export.FileExporter.FileType;

public class ArquivoImportFileFilter implements FileFilter {

	private Date dataModificacao;
	
	private FileType fileType;
		
	public ArquivoImportFileFilter(Date dataModificacao, FileType fileType) {
		this.dataModificacao = dataModificacao;
		this.fileType = fileType;
	}

	@Override
	public boolean accept(File pathname) {
		
		Long diferencaEmDias = DateUtil.obterDiferencaDias(new Date(pathname.lastModified()), dataModificacao);
		
		if (diferencaEmDias == 0) {
		
			String extension = getExtensionFile(pathname.getName());
			
			if (extension != null) {
				
				if (extension.equals(this.fileType.getExtension())) {
					return true;
				}
			}
		}
				
		return false;
	}

	/**
	 * Obtem a extensão do arquivo pelo nome.
	 * 
	 * @param fileName nome do arquivo
	 * @return extensão do arquivo no formato ".ext"
	 */
	private static String getExtensionFile(String fileName) {
		
		String extension = null;
		
		extension = fileName.substring(fileName.lastIndexOf("."));
		
		return extension;
	}
	
	/**
	 * @return the dataModificacao
	 */
	public Date getDataModificacao() {
		return dataModificacao;
	}

	/**
	 * @param dataModificacao the dataModificacao to set
	 */
	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}

	/**
	 * @return the fileType
	 */
	public FileType getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}
			
}
