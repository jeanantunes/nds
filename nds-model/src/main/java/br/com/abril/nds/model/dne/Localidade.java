package br.com.abril.nds.model.dne;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_LOCALIDADES")
public class Localidade implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4438288966441513488L;

	@Id
	@Column(name="CHAVE_LOC_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="ABREV_LOC_REC_ECT", length=36)
	private String abreviatura;

	@Column(name="CEP_LOCALIDADE", length=8)
	private String cep;

	@Column(name="CHAVE_SUB_LOC_DNE")
	private Long chaveSubordinacao;

	@Column(name="CODIGO_MUN_IBGE", length=7)
	private String codigoMunicipioIBGE;

	@Column(name="NOME_OFI_LOCALIDADE", length=72)
	private String nome;

	@Column(name="SIGLA_DR_ECT_LOC", length=3)
	private String siglaDR;

	@Column(name="SITUACAO_LOCALIDADE", length=1)
	private String situacao;

	@Column(name="TIPO_LOCALIDADE", length=1)
	private String tipoLocalidade;

	@OneToMany(mappedBy="localidade")
	private List<Bairro> bairros;

	@OneToMany(mappedBy="localidade")
	private List<CaixaPostalComunitaria> caixasPostaisComunitarias;

	@OneToMany(mappedBy="localidade")
	private List<FaixaCepLocalidade> faixasCepLocalidades;

	@OneToMany(mappedBy="localidade")
	private List<GrandeUsuario> grandesUsuarios;

	@OneToOne
	@JoinColumn(name="CHAVE_LOC_DNE", nullable=false, insertable=false, updatable=false)
	private Localidade localidade1;

	@OneToOne(mappedBy="localidade1")
	private Localidade localidade2;

    @ManyToOne
	@JoinColumn(name="SIGLA_UF")
	private UnidadeFederacao unidadeFederacao;

	@OneToMany(mappedBy="localidade")
	private List<Logradouro> logradouros;

	@OneToMany(mappedBy="localidade")
	private List<UnidadeOperacional> unidadesOperacionais;

    public Localidade() {
    }

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
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
	public String getCodigoMunicipioIBGE() {
		return codigoMunicipioIBGE;
	}

	/**
	 * @param codigoMunicipioIBGE the codigoMunicipioIBGE to set
	 */
	public void setCodigoMunicipioIBGE(String codigoMunicipioIBGE) {
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
	 * @return the siglaDR
	 */
	public String getSiglaDR() {
		return siglaDR;
	}

	/**
	 * @param siglaDR the siglaDR to set
	 */
	public void setSiglaDR(String siglaDR) {
		this.siglaDR = siglaDR;
	}

	/**
	 * @return the situacao
	 */
	public String getSituacao() {
		return situacao;
	}

	/**
	 * @param situacao the situacao to set
	 */
	public void setSituacao(String situacao) {
		this.situacao = situacao;
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
	 * @return the caixasPostaisComunitarias
	 */
	public List<CaixaPostalComunitaria> getCaixasPostaisComunitarias() {
		return caixasPostaisComunitarias;
	}

	/**
	 * @param caixasPostaisComunitarias the caixasPostaisComunitarias to set
	 */
	public void setCaixasPostaisComunitarias(
			List<CaixaPostalComunitaria> caixasPostaisComunitarias) {
		this.caixasPostaisComunitarias = caixasPostaisComunitarias;
	}

	/**
	 * @return the faixasCepLocalidades
	 */
	public List<FaixaCepLocalidade> getFaixasCepLocalidades() {
		return faixasCepLocalidades;
	}

	/**
	 * @param faixasCepLocalidades the faixasCepLocalidades to set
	 */
	public void setFaixasCepLocalidades(
			List<FaixaCepLocalidade> faixasCepLocalidades) {
		this.faixasCepLocalidades = faixasCepLocalidades;
	}

	/**
	 * @return the grandesUsuarios
	 */
	public List<GrandeUsuario> getGrandesUsuarios() {
		return grandesUsuarios;
	}

	/**
	 * @param grandesUsuarios the grandesUsuarios to set
	 */
	public void setGrandesUsuarios(List<GrandeUsuario> grandesUsuarios) {
		this.grandesUsuarios = grandesUsuarios;
	}

	/**
	 * @return the localidade1
	 */
	public Localidade getLocalidade1() {
		return localidade1;
	}

	/**
	 * @param localidade1 the localidade1 to set
	 */
	public void setLocalidade1(Localidade localidade1) {
		this.localidade1 = localidade1;
	}

	/**
	 * @return the localidade2
	 */
	public Localidade getLocalidade2() {
		return localidade2;
	}

	/**
	 * @param localidade2 the localidade2 to set
	 */
	public void setLocalidade2(Localidade localidade2) {
		this.localidade2 = localidade2;
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

	/**
	 * @return the unidadesOperacionais
	 */
	public List<UnidadeOperacional> getUnidadesOperacionais() {
		return unidadesOperacionais;
	}

	/**
	 * @param unidadesOperacionais the unidadesOperacionais to set
	 */
	public void setUnidadesOperacionais(
			List<UnidadeOperacional> unidadesOperacionais) {
		this.unidadesOperacionais = unidadesOperacionais;
	}

}