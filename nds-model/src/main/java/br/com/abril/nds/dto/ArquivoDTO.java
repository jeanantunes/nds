package br.com.abril.nds.dto;

import java.io.InputStream;
import java.io.Serializable;

import javax.activation.MimetypesFileTypeMap;

public class ArquivoDTO implements Serializable {

	private static final long serialVersionUID = 2355174705466848188L;

	private InputStream arquivo;
	private String nomeArquivo;	
	private String contentType;
	
	public ArquivoDTO() {
		
	}
	
	public ArquivoDTO(InputStream arquivo, String nomeArquivo, String contentType) {
		super();
		this.arquivo = arquivo;
		this.nomeArquivo = nomeArquivo;
		this.contentType = contentType;
	}
	/**
	 * @return the arquivo
	 */
	public InputStream getArquivo() {
		return arquivo;
	}
	/**
	 * @param arquivo the arquivo to set
	 */
	public void setArquivo(InputStream arquivo) {
		this.arquivo = arquivo;
	}
	/**
	 * @return the nomeArquivo
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	/**
	 * @param nomeArquivo the nomeArquivo to set
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}		
}
