package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.Endereco;

@Embeddable
public class InformacaoTransporte implements Serializable {


	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1459121120584386961L;
	
	/**
	 * modFrete
	 */
	@Column(name="MODALIDADE_FRENTE", length=1, nullable=false)
	private Integer modalidadeFrente;
	
	/**
	 * CNPJ
	 */
	@Column(name="CNPJ_TRANS", nullable=true, length=14)
	private String cnpj;	
	/**
	 * cpf
	 */
	@Column(name="CPF_TRANS", nullable=true, length=11)
	private String cpf;	
	
	/**
	 * xNome
	 */
	@Column(name="NOME_TRANS", nullable=true, length=60)
	private String nome;
	
	/**
	 * IE
	 */
	@Column(name="IE_TRANS", nullable=true, length=14)
	private String inscricaoEstadual;
	
	
	@OneToOne(optional=false)
	@JoinColumn(name="ENDERECO_ID_TRANS")
	private Endereco endereco;
	
	/**
	 * xMun
	 */
	@Column(name="MUN_TRANS", nullable=true, length=60)
	private String municipio;
	
	/**
	 * UF
	 */
	@Column(name="UF_TRANS", nullable=true, length=2)
	private String uf;
	
	@OneToOne(mappedBy="notaFiscal")
	private RetencaoICMSTransporte retencaoICMS;
	
	@Embedded
	private Veiculo veiculo;

	/**
	 * @return the modalidadeFrente
	 */
	public Integer getModalidadeFrente() {
		return modalidadeFrente;
	}

	/**
	 * @param modalidadeFrente the modalidadeFrente to set
	 */
	public void setModalidadeFrente(Integer modalidadeFrente) {
		this.modalidadeFrente = modalidadeFrente;
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
	 * @return the inscricaoEstadual
	 */
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	/**
	 * @param inscricaoEstadual the inscricaoEstadual to set
	 */
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	/**
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
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
	 * @return the retencaoICMS
	 */
	public RetencaoICMSTransporte getRetencaoICMS() {
		return retencaoICMS;
	}

	/**
	 * @param retencaoICMS the retencaoICMS to set
	 */
	public void setRetencaoICMS(RetencaoICMSTransporte retencaoICMS) {
		this.retencaoICMS = retencaoICMS;
	}

	/**
	 * @return the veiculo
	 */
	public Veiculo getVeiculo() {
		return veiculo;
	}

	/**
	 * @param veiculo the veiculo to set
	 */
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	/**
	 * Obtém endereco
	 *
	 * @return Endereco
	 */
	public Endereco getEndereco() {
		return endereco;
	}

	/**
	 * Atribuí endereco
	 * @param endereco 
	 */
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	

}
