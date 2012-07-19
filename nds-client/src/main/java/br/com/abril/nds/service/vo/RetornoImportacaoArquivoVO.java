package br.com.abril.nds.service.vo;

import java.io.Serializable;
import java.util.Arrays;

public class RetornoImportacaoArquivoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] motivoDoErro;
	
	private int numeroDaLinha;
	
	private String conteudoLinha;
	
	private boolean sucessoNaImportacao;
	
	/**
	 * Construtor padrão
	 */
	public RetornoImportacaoArquivoVO(){
		
	}
	
	public RetornoImportacaoArquivoVO(boolean sucessoNaImportacao){
		this.sucessoNaImportacao = sucessoNaImportacao;
	}
	
	/**
	 * Construtor
	 * @param motivoDoErro
	 * @param numeroDaLinha
	 * @param conteudoLinha
	 * @param sucessoNaImportacao
	 */
	public RetornoImportacaoArquivoVO(String[] motivoDoErro, int numeroDaLinha,
			String conteudoLinha, boolean sucessoNaImportacao) {
		super();
		this.motivoDoErro = motivoDoErro;
		this.numeroDaLinha = numeroDaLinha;
		this.conteudoLinha = conteudoLinha;
		this.sucessoNaImportacao = sucessoNaImportacao;
	}

	/**
	 * @return the motivoDoErro
	 */
	public String[] getMotivoDoErro() {
		return motivoDoErro;
	}

	/**
	 * @param motivoDoErro the motivoDoErro to set
	 */
	public void setMotivoDoErro(String[] motivoDoErro) {
		this.motivoDoErro = motivoDoErro;
	}

	/**
	 * @return the numeroDaLinha
	 */
	public int getNumeroDaLinha() {
		return numeroDaLinha;
	}

	/**
	 * @param numeroDaLinha the numeroDaLinha to set
	 */
	public void setNumeroDaLinha(int numeroDaLinha) {
		this.numeroDaLinha = numeroDaLinha;
	}

	/**
	 * @return the conteudoLinha
	 */
	public String getConteudoLinha() {
		return conteudoLinha;
	}

	/**
	 * @param conteudoLinha the conteudoLinha to set
	 */
	public void setConteudoLinha(String conteudoLinha) {
		this.conteudoLinha = conteudoLinha;
	}

	/**
	 * @return the sucessoNaImportacao
	 */
	public boolean isSucessoNaImportacao() {
		return sucessoNaImportacao;
	}

	/**
	 * @param sucessoNaImportacao the sucessoNaImportacao to set
	 */
	public void setSucessoNaImportacao(boolean sucessoNaImportacao) {
		this.sucessoNaImportacao = sucessoNaImportacao;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RetornoImportacaoArquivoVO [motivoDoErro="
				+ Arrays.toString(motivoDoErro) + ", numeroDaLinha="
				+ numeroDaLinha + ", conteudoLinha=" + conteudoLinha
				+ ", sucessoNaImportacao=" + sucessoNaImportacao + "]";
	}

}
