package br.com.abril.nds.model.dne;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_LOGRADOUROS")
public class Logradouro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3210879486950002174L;

	@Id
	@SequenceGenerator(name="DNE_GU_LOGRADOUROS_ID_GENERATOR", sequenceName="DNE_GU_LOGRADOUROS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DNE_GU_LOGRADOUROS_ID_GENERATOR")
	@Column(name="CHAVE_LOGRADOURO_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="ABREV_LOG_REC_ECT", length=36)
	private String abreviatura;

	@Column(name="CEP_LOGRADOURO", length=8)
	private String cep;

	@Column(name="CHAVE_BAI_FIM_DNE")
	private Long chaveBairroFinal;

	@Column(name="CHAVE_BAI_INI_DNE")
	private Long chaveBairroInicial;

	@Column(name="IND_EXIS_GU_LOG", length=1)
	private String flagExistencia;

	@Column(name="INFO_ADICIONAL", length=36)
	private String informacaoAdicional;

	@Column(name="NOME_OFI_LOGRADOURO", length=72)
	private String nome;

	@Column(length=3)
	private String preposicao;

	@Column(name="SIGLA_UF", length=2)
	private String uf;

	@Column(name="TIPO_OFI_LOGRADOURO", length=26)
	private String tipoLogradouro;

	@Column(name="TIT_PAT_OFI_LOGRADOURO", length=72)
	private String tituloOuPatenteLogradouro;

    @ManyToOne
	@JoinColumn(name="CHAVE_LOC_DNE", nullable=false)
	private Localidade localidade;

	@OneToMany(mappedBy="dneGuLogradouro")
	private List<Lote> lotes;

	@OneToMany(mappedBy="dneGuLogradouro")
	private List<Seccionamento> seccionamentos;

    public Logradouro() {
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
	 * @return the flagExistencia
	 */
	public String getFlagExistencia() {
		return flagExistencia;
	}

	/**
	 * @param flagExistencia the flagExistencia to set
	 */
	public void setFlagExistencia(String flagExistencia) {
		this.flagExistencia = flagExistencia;
	}

	/**
	 * @return the informacaoAdicional
	 */
	public String getInformacaoAdicional() {
		return informacaoAdicional;
	}

	/**
	 * @param informacaoAdicional the informacaoAdicional to set
	 */
	public void setInformacaoAdicional(String informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
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
	 * @return the preposicao
	 */
	public String getPreposicao() {
		return preposicao;
	}

	/**
	 * @param preposicao the preposicao to set
	 */
	public void setPreposicao(String preposicao) {
		this.preposicao = preposicao;
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
	 * @return the tituloOuPatenteLogradouro
	 */
	public String getTituloOuPatenteLogradouro() {
		return tituloOuPatenteLogradouro;
	}

	/**
	 * @param tituloOuPatenteLogradouro the tituloOuPatenteLogradouro to set
	 */
	public void setTituloOuPatenteLogradouro(String tituloOuPatenteLogradouro) {
		this.tituloOuPatenteLogradouro = tituloOuPatenteLogradouro;
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

	/**
	 * @return the lotes
	 */
	public List<Lote> getLotes() {
		return lotes;
	}

	/**
	 * @param lotes the lotes to set
	 */
	public void setLotes(List<Lote> lotes) {
		this.lotes = lotes;
	}

	/**
	 * @return the seccionamentos
	 */
	public List<Seccionamento> getSeccionamentos() {
		return seccionamentos;
	}

	/**
	 * @param seccionamentos the seccionamentos to set
	 */
	public void setSeccionamentos(List<Seccionamento> seccionamentos) {
		this.seccionamentos = seccionamentos;
	}

}