package br.com.abril.nds.dto;

import java.io.Serializable;

/**
 * DTO com as propriedades necessárias para se realizar a impressão de uma procuração.
 * 
 * @author Discover Technology
 *
 */
public class ProcuracaoImpressaoDTO implements Serializable {

	private static final long serialVersionUID = -3792059615495193266L;

	private String nomeJornaleiro;
	
	private String boxCota;
	
	private String nacionalidade;
	
	private String estadoCivil;
	
	private String enderecoPDV;
	
	private String bairroPDV;
	
	private String cidadePDV;
	
	private String numeroPermissao;
	
	private String rgJornaleiro;
	
	private String cpfJornaleiro;
	
	private String nomeProcurador;
	
	private String rgProcurador;
	
	private String estadoCivilProcurador;
	
	private String nacionalidadeProcurador;
	
	private String enderecoDoProcurado;
	
	private String bairroProcurado;
	
	private String cidadeProcurado;

	/**
	 * @return the nomeJornaleiro
	 */
	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}

	/**
	 * @param nomeJornaleiro the nomeJornaleiro to set
	 */
	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}

	/**
	 * @return the boxCota
	 */
	public String getBoxCota() {
		return boxCota;
	}

	/**
	 * @param boxCota the boxCota to set
	 */
	public void setBoxCota(String boxCota) {
		this.boxCota = boxCota;
	}

	/**
	 * @return the nacionalidade
	 */
	public String getNacionalidade() {
		return nacionalidade;
	}

	/**
	 * @param nacionalidade the nacionalidade to set
	 */
	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	/**
	 * @return the estadoCivil
	 */
	public String getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * @param estadoCivil the estadoCivil to set
	 */
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	/**
	 * @return the enderecoPDV
	 */
	public String getEnderecoPDV() {
		return enderecoPDV;
	}

	/**
	 * @param enderecoPDV the enderecoPDV to set
	 */
	public void setEnderecoPDV(String enderecoPDV) {
		this.enderecoPDV = enderecoPDV;
	}

	/**
	 * @return the bairroPDV
	 */
	public String getBairroPDV() {
		return bairroPDV;
	}

	/**
	 * @param bairroPDV the bairroPDV to set
	 */
	public void setBairroPDV(String bairroPDV) {
		this.bairroPDV = bairroPDV;
	}

	/**
	 * @return the cidadePDV
	 */
	public String getCidadePDV() {
		return cidadePDV;
	}

	/**
	 * @param cidadePDV the cidadePDV to set
	 */
	public void setCidadePDV(String cidadePDV) {
		this.cidadePDV = cidadePDV;
	}

	/**
	 * @return the numeroPermissao
	 */
	public String getNumeroPermissao() {
		return numeroPermissao;
	}

	/**
	 * @param numeroPermissao the numeroPermissao to set
	 */
	public void setNumeroPermissao(String numeroPermissao) {
		this.numeroPermissao = numeroPermissao;
	}

	/**
	 * @return the rgJornaleiro
	 */
	public String getRgJornaleiro() {
		return rgJornaleiro;
	}

	/**
	 * @param rgJornaleiro the rgJornaleiro to set
	 */
	public void setRgJornaleiro(String rgJornaleiro) {
		this.rgJornaleiro = rgJornaleiro;
	}

	/**
	 * @return the cpfJornaleiro
	 */
	public String getCpfJornaleiro() {
		return cpfJornaleiro;
	}

	/**
	 * @param cpfJornaleiro the cpfJornaleiro to set
	 */
	public void setCpfJornaleiro(String cpfJornaleiro) {
		this.cpfJornaleiro = cpfJornaleiro;
	}

	/**
	 * @return the nomeProcurador
	 */
	public String getNomeProcurador() {
		return nomeProcurador;
	}

	/**
	 * @param nomeProcurador the nomeProcurador to set
	 */
	public void setNomeProcurador(String nomeProcurador) {
		this.nomeProcurador = nomeProcurador;
	}

	/**
	 * @return the rgProcurador
	 */
	public String getRgProcurador() {
		return rgProcurador;
	}

	/**
	 * @param rgProcurador the rgProcurador to set
	 */
	public void setRgProcurador(String rgProcurador) {
		this.rgProcurador = rgProcurador;
	}

	/**
	 * @return the estadoCivilProcurador
	 */
	public String getEstadoCivilProcurador() {
		return estadoCivilProcurador;
	}

	/**
	 * @param estadoCivilProcurador the estadoCivilProcurador to set
	 */
	public void setEstadoCivilProcurador(String estadoCivilProcurador) {
		this.estadoCivilProcurador = estadoCivilProcurador;
	}

	/**
	 * @return the nacionalidadeProcurador
	 */
	public String getNacionalidadeProcurador() {
		return nacionalidadeProcurador;
	}

	/**
	 * @param nacionalidadeProcurador the nacionalidadeProcurador to set
	 */
	public void setNacionalidadeProcurador(String nacionalidadeProcurador) {
		this.nacionalidadeProcurador = nacionalidadeProcurador;
	}

	/**
	 * @return the enderecoDoProcurado
	 */
	public String getEnderecoDoProcurado() {
		return enderecoDoProcurado;
	}

	/**
	 * @param enderecoDoProcurado the enderecoDoProcurado to set
	 */
	public void setEnderecoDoProcurado(String enderecoDoProcurado) {
		this.enderecoDoProcurado = enderecoDoProcurado;
	}

	/**
	 * @return the bairroProcurado
	 */
	public String getBairroProcurado() {
		return bairroProcurado;
	}

	/**
	 * @param bairroProcurado the bairroProcurado to set
	 */
	public void setBairroProcurado(String bairroProcurado) {
		this.bairroProcurado = bairroProcurado;
	}

	/**
	 * @return the cidadeProcurado
	 */
	public String getCidadeProcurado() {
		return cidadeProcurado;
	}

	/**
	 * @param cidadeProcurado the cidadeProcurado to set
	 */
	public void setCidadeProcurado(String cidadeProcurado) {
		this.cidadeProcurado = cidadeProcurado;
	}
}
