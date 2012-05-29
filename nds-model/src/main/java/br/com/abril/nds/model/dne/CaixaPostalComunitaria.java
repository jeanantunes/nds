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
@Table(name="DNE_GU_CAIXAS_POSTAIS_COMUNIT")
public class CaixaPostalComunitaria implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 517200942458327946L;

	@Id
	@SequenceGenerator(name="DNE_GU_CAIXAS_POSTAIS_COMUNIT_ID_GENERATOR", sequenceName="DNE_GU_CAIXAS_POSTAIS_COMUNIT_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DNE_GU_CAIXAS_POSTAIS_COMUNIT_ID_GENERATOR")
	@Column(name="CHAVE_CX_POSTAL_COMU", unique=true, nullable=false)
	private String id;

	@Column(name="AREA_ABRAN_CX_POSTAL_COMU", length=72)
	private String areaAbrangencia;

	@Column(name="CEP_PT_CX_POSTAL_COMU", length=8)
	private String cep;

	@Column(name="END_PT_CX_POSTAL_COMU", length=72)
	private String endereco;

	@Column(name="NOME_PT_CX_POSTAL_COMU", length=72)
	private String nome;

	@Column(name="NUM_FIM_CX_POSTAL_COMU")
	private Long numeroFinal;

	@Column(name="NUM_INI_CX_POSTAL_COMU")
	private Long numeroInicial;

	@Column(name="SIGLA_UF", length=2)
	private String uf;
	
    @ManyToOne
	@JoinColumn(name="CHAVE_LOC_DNE", nullable=false)
	private Localidade localidade;

    public CaixaPostalComunitaria() {
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
	 * @return the areaAbrangencia
	 */
	public String getAreaAbrangencia() {
		return areaAbrangencia;
	}

	/**
	 * @param areaAbrangencia the areaAbrangencia to set
	 */
	public void setAreaAbrangencia(String areaAbrangencia) {
		this.areaAbrangencia = areaAbrangencia;
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
	 * @return the numeroFinal
	 */
	public Long getNumeroFinal() {
		return numeroFinal;
	}

	/**
	 * @param numeroFinal the numeroFinal to set
	 */
	public void setNumeroFinal(Long numeroFinal) {
		this.numeroFinal = numeroFinal;
	}

	/**
	 * @return the numeroInicial
	 */
	public Long getNumeroInicial() {
		return numeroInicial;
	}

	/**
	 * @param numeroInicial the numeroInicial to set
	 */
	public void setNumeroInicial(Long numeroInicial) {
		this.numeroInicial = numeroInicial;
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