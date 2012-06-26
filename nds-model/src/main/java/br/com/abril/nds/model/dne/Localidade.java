package br.com.abril.nds.model.dne;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="LOG_LOCALIDADE")
public class Localidade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4438288966441513488L;

	@Id
	@Column(name="LOC_NU", unique=true, nullable=false)
	private Long id;

	@Column(name="LOC_NO_ABREV", length=72)
	private String abreviatura;

	@Column(name="CEP", length=16)
	private String cep;

	@Column(name="LOC_NU_SUB")
	private Long chaveSubordinacao;

	@Column(name="MUN_NU")
	private Long codigoMunicipioIBGE;

	@Column(name="LOC_NO", length=144)
	private String nome;

	@Column(name="LOC_IN_TIPO_LOC", length=2)
	private String tipoLocalidade;

	@Column(name="LOC_IN_SIT", length=2)
	private String sit;

    @ManyToOne
	@JoinColumn(name="UFE_SG")
	private UnidadeFederacao unidadeFederacao;

	@OneToMany(mappedBy="localidade")
	private List<Bairro> bairros;

	@OneToMany(mappedBy="localidade")
	private List<Logradouro> logradouros;

    public Localidade() {
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
	 * @return the chaveSubordinacao
	 */
	public Long getChaveSubordinacao() {
		return chaveSubordinacao;
	}

	/**
	 * @param chaveSubordinacao the chaveSubordinacao to set
	 */
	public void setChaveSubordinacao(Long chaveSubordinacao) {
		this.chaveSubordinacao = chaveSubordinacao;
	}

	/**
	 * @return the codigoMunicipioIBGE
	 */
	public Long getCodigoMunicipioIBGE() {
		return codigoMunicipioIBGE;
	}

	/**
	 * @param codigoMunicipioIBGE the codigoMunicipioIBGE to set
	 */
	public void setCodigoMunicipioIBGE(Long codigoMunicipioIBGE) {
		this.codigoMunicipioIBGE = codigoMunicipioIBGE;
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
	 * @return the tipoLocalidade
	 */
	public String getTipoLocalidade() {
		return tipoLocalidade;
	}

	/**
	 * @param tipoLocalidade the tipoLocalidade to set
	 */
	public void setTipoLocalidade(String tipoLocalidade) {
		this.tipoLocalidade = tipoLocalidade;
	}

	/**
	 * @return the sit
	 */
	public String getSit() {
		return sit;
	}

	/**
	 * @param sit the sit to set
	 */
	public void setSit(String sit) {
		this.sit = sit;
	}

	/**
	 * @return the unidadeFederacao
	 */
	public UnidadeFederacao getUnidadeFederacao() {
		return unidadeFederacao;
	}

	/**
	 * @param unidadeFederacao the unidadeFederacao to set
	 */
	public void setUnidadeFederacao(UnidadeFederacao unidadeFederacao) {
		this.unidadeFederacao = unidadeFederacao;
	}

	/**
	 * @return the bairros
	 */
	public List<Bairro> getBairros() {
		return bairros;
	}

	/**
	 * @param bairros the bairros to set
	 */
	public void setBairros(List<Bairro> bairros) {
		this.bairros = bairros;
	}

	/**
	 * @return the logradouros
	 */
	public List<Logradouro> getLogradouros() {
		return logradouros;
	}

	/**
	 * @param logradouros the logradouros to set
	 */
	public void setLogradouros(List<Logradouro> logradouros) {
		this.logradouros = logradouros;
	}

}