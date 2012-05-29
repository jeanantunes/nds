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
@Table(name="DNE_GU_GRANDES_USUARIOS")
public class GrandeUsuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1369950716913195740L;

	@Id
	@Column(name="CHAVE_GU_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="ABREV_GU_REC_ECT", length=36)
	private String abreviatura;

	@Column(name="CEP_GU", length=8)
	private String cep;

	@Column(name="CHAVE_BAI_DNE")
	private Long chaveBairro;

	@Column(name="CHAVE_LOG_DNE")
	private Long chaveLogradouro;

	@Column(name="NOME_COMPL1", length=36)
	private String nomeComplemento1;

	@Column(name="NOME_COMPL2", length=36)
	private String nomeComplemento2;

	@Column(name="NOME_OFI_GU", length=72)
	private String nome;

	@Column(name="NOME_OFI_LOG", length=72)
	private String nomeLogradouro;

	@Column(name="NUM_LETR_COMPL1", length=11)
	private String numeroOuLetraComplemento1;

	@Column(name="NUM_LETR_COMPL2", length=11)
	private String numeroOuLetraComplemento2;

	@Column(name="NUM_LETR_UNID_OCUP", length=36)
	private String numeroOuLetraUnidadeOcupacional;

	@Column(name="NUM_LOTE", length=11)
	private String numeroLote;

	@Column(length=3)
	private String preposicao;

	@Column(name="SIGLA_UF", length=2)
	private String uf;

	@Column(name="TIPO_OFI_LOGRADOURO", length=72)
	private String tipoLogradouro;

	@Column(name="TIPO_OFI_UNID_OCUP", length=36)
	private String tipoUnidadeOcupacional;

	@Column(name="TIT_PAT_OFI_LOG", length=72)
	private String tituloOuPatenteLogradouro;

	@ManyToOne
	@JoinColumn(name="CHAVE_LOC_DNE", nullable=false)
	private Localidade localidade;

    public GrandeUsuario() {
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
	 * @return the chaveBairro
	 */
	public Long getChaveBairro() {
		return chaveBairro;
	}

	/**
	 * @param chaveBairro the chaveBairro to set
	 */
	public void setChaveBairro(Long chaveBairro) {
		this.chaveBairro = chaveBairro;
	}

	/**
	 * @return the chaveLogradouro
	 */
	public Long getChaveLogradouro() {
		return chaveLogradouro;
	}

	/**
	 * @param chaveLogradouro the chaveLogradouro to set
	 */
	public void setChaveLogradouro(Long chaveLogradouro) {
		this.chaveLogradouro = chaveLogradouro;
	}

	/**
	 * @return the nomeComplemento1
	 */
	public String getNomeComplemento1() {
		return nomeComplemento1;
	}

	/**
	 * @param nomeComplemento1 the nomeComplemento1 to set
	 */
	public void setNomeComplemento1(String nomeComplemento1) {
		this.nomeComplemento1 = nomeComplemento1;
	}

	/**
	 * @return the nomeComplemento2
	 */
	public String getNomeComplemento2() {
		return nomeComplemento2;
	}

	/**
	 * @param nomeComplemento2 the nomeComplemento2 to set
	 */
	public void setNomeComplemento2(String nomeComplemento2) {
		this.nomeComplemento2 = nomeComplemento2;
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
	 * @return the nomeLogradouro
	 */
	public String getNomeLogradouro() {
		return nomeLogradouro;
	}

	/**
	 * @param nomeLogradouro the nomeLogradouro to set
	 */
	public void setNomeLogradouro(String nomeLogradouro) {
		this.nomeLogradouro = nomeLogradouro;
	}

	/**
	 * @return the numeroOuLetraComplemento1
	 */
	public String getNumeroOuLetraComplemento1() {
		return numeroOuLetraComplemento1;
	}

	/**
	 * @param numeroOuLetraComplemento1 the numeroOuLetraComplemento1 to set
	 */
	public void setNumeroOuLetraComplemento1(String numeroOuLetraComplemento1) {
		this.numeroOuLetraComplemento1 = numeroOuLetraComplemento1;
	}

	/**
	 * @return the numeroOuLetraComplemento2
	 */
	public String getNumeroOuLetraComplemento2() {
		return numeroOuLetraComplemento2;
	}

	/**
	 * @param numeroOuLetraComplemento2 the numeroOuLetraComplemento2 to set
	 */
	public void setNumeroOuLetraComplemento2(String numeroOuLetraComplemento2) {
		this.numeroOuLetraComplemento2 = numeroOuLetraComplemento2;
	}

	/**
	 * @return the numeroOuLetraUnidadeOcupacional
	 */
	public String getNumeroOuLetraUnidadeOcupacional() {
		return numeroOuLetraUnidadeOcupacional;
	}

	/**
	 * @param numeroOuLetraUnidadeOcupacional the numeroOuLetraUnidadeOcupacional to set
	 */
	public void setNumeroOuLetraUnidadeOcupacional(
			String numeroOuLetraUnidadeOcupacional) {
		this.numeroOuLetraUnidadeOcupacional = numeroOuLetraUnidadeOcupacional;
	}

	/**
	 * @return the numeroLote
	 */
	public String getNumeroLote() {
		return numeroLote;
	}

	/**
	 * @param numeroLote the numeroLote to set
	 */
	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
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
	 * @return the tipoUnidadeOcupacional
	 */
	public String getTipoUnidadeOcupacional() {
		return tipoUnidadeOcupacional;
	}

	/**
	 * @param tipoUnidadeOcupacional the tipoUnidadeOcupacional to set
	 */
	public void setTipoUnidadeOcupacional(String tipoUnidadeOcupacional) {
		this.tipoUnidadeOcupacional = tipoUnidadeOcupacional;
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

}