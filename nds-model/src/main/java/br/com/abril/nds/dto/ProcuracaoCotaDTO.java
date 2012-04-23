package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.EstadoCivil;

public class ProcuracaoCotaDTO implements Serializable {
	
	private static final long serialVersionUID = 4715132531410204087L;

	private Integer numeroCota;
	
	private String nomeJornaleiro;
	
	private String box;
	
	private String nacionalidade;
	
	private EstadoCivil estadoCivil;
	
	private String enderecoPDVPrincipal;
	
	private String cep;
	
	private String bairro;
	
	private String cidade;
	
	private Long numeroPermissao;
	
	private String rg;
	
	private String cpf;
	
	private String nomeProcurador;
	
	private EstadoCivil estadoCivilProcurador;
	
	private String enderecoProcurador;
	
	private String nacionalidadeProcurador;
	
	private String rgProcurador;
	
	private String profissaoProcurador;

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

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
	 * @return the box
	 */
	public String getBox() {
		return box;
	}

	/**
	 * @param box the box to set
	 */
	public void setBox(String box) {
		this.box = box;
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
	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * @param estadoCivil the estadoCivil to set
	 */
	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	/**
	 * @return the enderecoPDVPrincipal
	 */
	public String getEnderecoPDVPrincipal() {
		return enderecoPDVPrincipal;
	}

	/**
	 * @param enderecoPDVPrincipal the enderecoPDVPrincipal to set
	 */
	public void setEnderecoPDVPrincipal(String enderecoPDVPrincipal) {
		this.enderecoPDVPrincipal = enderecoPDVPrincipal;
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
	 * @return the bairro
	 */
	public String getBairro() {
		return bairro;
	}

	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
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
	 * @return the numeroPermissao
	 */
	public Long getNumeroPermissao() {
		return numeroPermissao;
	}

	/**
	 * @param numeroPermissao the numeroPermissao to set
	 */
	public void setNumeroPermissao(Long numeroPermissao) {
		this.numeroPermissao = numeroPermissao;
	}

	/**
	 * @return the rg
	 */
	public String getRg() {
		return rg;
	}

	/**
	 * @param rg the rg to set
	 */
	public void setRg(String rg) {
		this.rg = rg;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return cpf;
	}

	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
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
	 * @return the estadoCivilProcurador
	 */
	public EstadoCivil getEstadoCivilProcurador() {
		return estadoCivilProcurador;
	}

	/**
	 * @param estadoCivilProcurador the estadoCivilProcurador to set
	 */
	public void setEstadoCivilProcurador(EstadoCivil estadoCivilProcurador) {
		this.estadoCivilProcurador = estadoCivilProcurador;
	}

	/**
	 * @return the enderecoProcurador
	 */
	public String getEnderecoProcurador() {
		return enderecoProcurador;
	}

	/**
	 * @param enderecoProcurador the enderecoProcurador to set
	 */
	public void setEnderecoProcurador(String enderecoProcurador) {
		this.enderecoProcurador = enderecoProcurador;
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
	 * @return the profissaoProcurador
	 */
	public String getProfissaoProcurador() {
		return profissaoProcurador;
	}

	/**
	 * @param profissaoProcurador the profissaoProcurador to set
	 */
	public void setProfissaoProcurador(String profissaoProcurador) {
		this.profissaoProcurador = profissaoProcurador;
	}
}
