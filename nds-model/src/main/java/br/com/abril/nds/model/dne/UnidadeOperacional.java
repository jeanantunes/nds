package br.com.abril.nds.model.dne;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_UNIDADES_OPERACIONAIS")
public class UnidadeOperacional implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8572653200020832789L;

	@Id
	@SequenceGenerator(name="DNE_GU_UNIDADES_OPERACIONAIS_ID_GENERATOR", sequenceName="DNE_GU_UNIDADES_OPERACIONAIS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DNE_GU_UNIDADES_OPERACIONAIS_ID_GENERATOR")
	@Column(name="CHAVE_UNID_OPER_DNE", unique=true, nullable=false, length=18)
	private String id;

	@Column(name="ABREV_UNID_OPER_REC_ECT", length=36)
	private String abreviatura;

	@Column(name="CEP_UNID_OPER", length=8)
	private String cep;

	@Column(name="CHAVE_BAI_DNE")
	private Long chaveBairro;

	@Column(name="CHAVE_LOG_DNE")
	private Long chaveLogradouro;

	@Column(name="NOME_COMPL1", length=36)
	private String nomeComplemento1;

	@Column(name="NOME_COMPL2", length=36)
	private String nomeComplemento2;

	@Column(name="NOME_OFI_UNID_OPER", length=72)
	private String nome;

	@Column(name="NUM_FIM_CX_POSTAL_1")
	private Long numeroFinalCaixaPostal1;

	@Column(name="NUM_FIM_CX_POSTAL_2")
	private Long numeroFinalCaixaPostal2;

	@Column(name="NUM_FIM_CX_POSTAL_3")
	private Long numeroFinalCaixaPostal3;

	@Column(name="NUM_INI_CX_POSTAL_1")
	private Long numeroInicialCaixaPosta1;

	@Column(name="NUM_INI_CX_POSTAL_2")
	private Long numeroInicialCaixaPosta2;

	@Column(name="NUM_INI_CX_POSTAL_3")
	private Long numeroInicialCaixaPosta3;

	@Column(name="NUM_LET_COMPL1", length=11)
	private String numeroOuLetraComplemento1;

	@Column(name="NUM_LET_COMPL2", length=11)
	private String numeroOuLetraComplemento2;

	@Column(name="NUM_LET_UNID_OCUP", length=36)
	private String numeroOuLetra;

	@Column(name="NUM_LOTE", length=11)
	private String lote;

	@Column(length=3)
	private String preposicao;

	@Column(name="SIGLA_UF", length=2)
	private String uf;

	@Column(name="TIPO_NUM_CX_POSTAL_1", length=1)
	private String tipoNumeracaoCaixaPostal1;

	@Column(name="TIPO_NUM_CX_POSTAL_2", length=1)
	private String tipoNumeracaoCaixaPostal2;

	@Column(name="TIPO_NUM_CX_POSTAL_3", length=1)
	private String tipoNumeracaoCaixaPostal3;

	@Column(name="TIPO_OFI_LOG", length=72)
	private String tipoLogradouro;

	@Column(name="TIPO_OFI_UNID_OCUP", length=36)
	private String tipoUnidadeOcupacional;

	@Column(name="TIPO_UNID_OPER", length=72)
	private String tipoUnidadeOperacional;

	@Column(name="TIT_PAT_OFI_LOG", length=72)
	private String tituloOuPatenteLogradouro;

    @ManyToOne
	@JoinColumn(name="CHAVE_LOC_DNE", nullable=false)
	private Localidade localidade;

    public UnidadeOperacional() {
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
	 * @return the numeroFinalCaixaPostal1
	 */
	public Long getNumeroFinalCaixaPostal1() {
		return numeroFinalCaixaPostal1;
	}

	/**
	 * @param numeroFinalCaixaPostal1 the numeroFinalCaixaPostal1 to set
	 */
	public void setNumeroFinalCaixaPostal1(Long numeroFinalCaixaPostal1) {
		this.numeroFinalCaixaPostal1 = numeroFinalCaixaPostal1;
	}

	/**
	 * @return the numeroFinalCaixaPostal2
	 */
	public Long getNumeroFinalCaixaPostal2() {
		return numeroFinalCaixaPostal2;
	}

	/**
	 * @param numeroFinalCaixaPostal2 the numeroFinalCaixaPostal2 to set
	 */
	public void setNumeroFinalCaixaPostal2(Long numeroFinalCaixaPostal2) {
		this.numeroFinalCaixaPostal2 = numeroFinalCaixaPostal2;
	}

	/**
	 * @return the numeroFinalCaixaPostal3
	 */
	public Long getNumeroFinalCaixaPostal3() {
		return numeroFinalCaixaPostal3;
	}

	/**
	 * @param numeroFinalCaixaPostal3 the numeroFinalCaixaPostal3 to set
	 */
	public void setNumeroFinalCaixaPostal3(Long numeroFinalCaixaPostal3) {
		this.numeroFinalCaixaPostal3 = numeroFinalCaixaPostal3;
	}

	/**
	 * @return the numeroInicialCaixaPosta1
	 */
	public Long getNumeroInicialCaixaPosta1() {
		return numeroInicialCaixaPosta1;
	}

	/**
	 * @param numeroInicialCaixaPosta1 the numeroInicialCaixaPosta1 to set
	 */
	public void setNumeroInicialCaixaPosta1(Long numeroInicialCaixaPosta1) {
		this.numeroInicialCaixaPosta1 = numeroInicialCaixaPosta1;
	}

	/**
	 * @return the numeroInicialCaixaPosta2
	 */
	public Long getNumeroInicialCaixaPosta2() {
		return numeroInicialCaixaPosta2;
	}

	/**
	 * @param numeroInicialCaixaPosta2 the numeroInicialCaixaPosta2 to set
	 */
	public void setNumeroInicialCaixaPosta2(Long numeroInicialCaixaPosta2) {
		this.numeroInicialCaixaPosta2 = numeroInicialCaixaPosta2;
	}

	/**
	 * @return the numeroInicialCaixaPosta3
	 */
	public Long getNumeroInicialCaixaPosta3() {
		return numeroInicialCaixaPosta3;
	}

	/**
	 * @param numeroInicialCaixaPosta3 the numeroInicialCaixaPosta3 to set
	 */
	public void setNumeroInicialCaixaPosta3(Long numeroInicialCaixaPosta3) {
		this.numeroInicialCaixaPosta3 = numeroInicialCaixaPosta3;
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
	 * @return the numeroOuLetra
	 */
	public String getNumeroOuLetra() {
		return numeroOuLetra;
	}

	/**
	 * @param numeroOuLetra the numeroOuLetra to set
	 */
	public void setNumeroOuLetra(String numeroOuLetra) {
		this.numeroOuLetra = numeroOuLetra;
	}

	/**
	 * @return the lote
	 */
	public String getLote() {
		return lote;
	}

	/**
	 * @param lote the lote to set
	 */
	public void setLote(String lote) {
		this.lote = lote;
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
	 * @return the tipoNumeracaoCaixaPostal1
	 */
	public String getTipoNumeracaoCaixaPostal1() {
		return tipoNumeracaoCaixaPostal1;
	}

	/**
	 * @param tipoNumeracaoCaixaPostal1 the tipoNumeracaoCaixaPostal1 to set
	 */
	public void setTipoNumeracaoCaixaPostal1(String tipoNumeracaoCaixaPostal1) {
		this.tipoNumeracaoCaixaPostal1 = tipoNumeracaoCaixaPostal1;
	}

	/**
	 * @return the tipoNumeracaoCaixaPostal2
	 */
	public String getTipoNumeracaoCaixaPostal2() {
		return tipoNumeracaoCaixaPostal2;
	}

	/**
	 * @param tipoNumeracaoCaixaPostal2 the tipoNumeracaoCaixaPostal2 to set
	 */
	public void setTipoNumeracaoCaixaPostal2(String tipoNumeracaoCaixaPostal2) {
		this.tipoNumeracaoCaixaPostal2 = tipoNumeracaoCaixaPostal2;
	}

	/**
	 * @return the tipoNumeracaoCaixaPostal3
	 */
	public String getTipoNumeracaoCaixaPostal3() {
		return tipoNumeracaoCaixaPostal3;
	}

	/**
	 * @param tipoNumeracaoCaixaPostal3 the tipoNumeracaoCaixaPostal3 to set
	 */
	public void setTipoNumeracaoCaixaPostal3(String tipoNumeracaoCaixaPostal3) {
		this.tipoNumeracaoCaixaPostal3 = tipoNumeracaoCaixaPostal3;
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
	 * @return the tipoUnidadeOperacional
	 */
	public String getTipoUnidadeOperacional() {
		return tipoUnidadeOperacional;
	}

	/**
	 * @param tipoUnidadeOperacional the tipoUnidadeOperacional to set
	 */
	public void setTipoUnidadeOperacional(String tipoUnidadeOperacional) {
		this.tipoUnidadeOperacional = tipoUnidadeOperacional;
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