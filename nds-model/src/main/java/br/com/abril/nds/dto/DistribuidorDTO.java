package br.com.abril.nds.dto;

import java.io.Serializable;

public class DistribuidorDTO implements Serializable {

	private static final long serialVersionUID = 6231385672190900756L;

	private String razaoSocial;
	private String endereco;
	private String cnpj;
	private String cidade;
	private String uf;
	private String cep;
	private String inscricaoEstatual;
	/**
	 * @return the razaoSocial
	 */
	public String getRazaoSocial() {
		return razaoSocial;
	}
	/**
	 * @param razaoSocial the razaoSocial to set
	 */
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	/**
	 * @return the endereco
	 */
	public String getEndereco() {
		return endereco;
	}
	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}
	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}
	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}
	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}
	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}
	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}
	/**
	 * @return the inscricaoEstatual
	 */
	public String getInscricaoEstatual() {
		return inscricaoEstatual;
	}
	/**
	 * @param inscricaoEstatual the inscricaoEstatual to set
	 */
	public void setInscricaoEstatual(String inscricaoEstatual) {
		this.inscricaoEstatual = inscricaoEstatual;
	}
	
	
}
