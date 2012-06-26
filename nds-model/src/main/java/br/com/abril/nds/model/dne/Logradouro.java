package br.com.abril.nds.model.dne;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="LOG_LOGRADOURO")
public class Logradouro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3210879486950002174L;

	@Id
	@Column(name="LOG_NU", unique=true, nullable=false)
	private Long id;

	@Column(name="LOG_NO_ABREV", length=72)
	private String abreviatura;

	@Column(name="CEP", length=16)
	private String cep;

	@Column(name="BAI_NU_FIM")
	private Long chaveBairroFinal;

	@Column(name="BAI_NU_INI")
	private Long chaveBairroInicial;

	@Column(name="LOG_STA_TLO", length=2)
	private String status;
	
	@Column(name="LOG_NO", length=144)
	private String nome;

	@Column(name="LOG_COMPLEMENTO", length=200)
	private String complemento;

	@Column(name="UFE_SG", length=4)
	private String uf;

	@Column(name="TLO_TX", length=72)
	private String tipoLogradouro;

    @ManyToOne
	@JoinColumn(name="LOC_NU", nullable=false)
	private Localidade localidade;

    public Logradouro() {
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the abreviatura
	 */
	public String getAbreviatura() {
		return abreviatura;
	}

	/**
	 * @param abreviatura the abreviatura to set
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
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
	 * @return the chaveBairroFinal
	 */
	public Long getChaveBairroFinal() {
		return chaveBairroFinal;
	}

	/**
	 * @param chaveBairroFinal the chaveBairroFinal to set
	 */
	public void setChaveBairroFinal(Long chaveBairroFinal) {
		this.chaveBairroFinal = chaveBairroFinal;
	}

	/**
	 * @return the chaveBairroInicial
	 */
	public Long getChaveBairroInicial() {
		return chaveBairroInicial;
	}

	/**
	 * @param chaveBairroInicial the chaveBairroInicial to set
	 */
	public void setChaveBairroInicial(Long chaveBairroInicial) {
		this.chaveBairroInicial = chaveBairroInicial;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the complemento
	 */
	public String getComplemento() {
		return complemento;
	}

	/**
	 * @param complemento the complemento to set
	 */
	public void setComplemento(String complemento) {
		this.complemento = complemento;
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
	 * @return the tipoLogradouro
	 */
	public String getTipoLogradouro() {
		return tipoLogradouro;
	}

	/**
	 * @param tipoLogradouro the tipoLogradouro to set
	 */
	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}

	/**
	 * @return the localidade
	 */
	public Localidade getLocalidade() {
		return localidade;
	}

	/**
	 * @param localidade the localidade to set
	 */
	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

}