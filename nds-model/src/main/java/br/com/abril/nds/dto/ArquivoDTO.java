package br.com.abril.nds.dto;

import java.io.InputStream;
import java.io.Serializable;

public class ArquivoDTO implements Serializable {

	private static final long serialVersionUID = 2355174705466848188L;

	private InputStream arquivo;
	private String nomeArquivo;	
	private String path;
	
	public ArquivoDTO() {
		
	}
	
	public ArquivoDTO(InputStream arquivo, String nomeArquivo, String path) {
		super();
		this.arquivo = arquivo;
		this.nomeArquivo = nomeArquivo;
		this.path = path;
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
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
		
}
