package br.com.abril.nds.model.dne;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_BAIRROS")
public class Bairro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2098584768905693662L;

	@Id
	@SequenceGenerator(name="DNE_GU_BAIRROS_ID_GENERATOR", sequenceName="DNE_GU_BAIRROS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DNE_GU_BAIRROS_ID_GENERATOR")
	@Column(name="CHAVE_BAI_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="ABRE_BAI_REC_ECT", length=36)
	private String abreviatura;

	@Column(name="NOME_OFI_BAI", length=72)
	private String nome;

	@Column(name="SIGLA_UF_BAI", length=2)
	private String uf;

	
    @ManyToOne
	@JoinColumn(name="CHAVE_LOC_DNE", nullable=false)
	private Localidade localidade;

	
	@OneToMany(mappedBy="dneGuBairro")
	private List<FaixaCepBairro> faixasCepBairros;

    public Bairro() {
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

	/**
	 * @return the faixasCepBairros
	 */
	public List<FaixaCepBairro> getFaixasCepBairros() {
		return faixasCepBairros;
	}

	/**
	 * @param faixasCepBairros the faixasCepBairros to set
	 */
	public void setFaixasCepBairros(List<FaixaCepBairro> faixasCepBairros) {
		this.faixasCepBairros = faixasCepBairros;
	}
	
}