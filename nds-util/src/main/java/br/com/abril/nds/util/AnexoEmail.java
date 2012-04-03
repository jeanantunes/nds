package br.com.abril.nds.util;

public class AnexoEmail {

	
	private String nome;
	private byte[] anexo;
	
	public AnexoEmail() {}
	
	public AnexoEmail(String nome, byte[] anexo) {
		this.nome = nome;
		this.anexo = anexo;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the anexo
	 */
	public byte[] getAnexo() {
		return anexo;
	}

	/**
	 * @param anexo the anexo to set
	 */
	public void setAnexo(byte[] anexo) {
		this.anexo = anexo;
	}
	
	
}
