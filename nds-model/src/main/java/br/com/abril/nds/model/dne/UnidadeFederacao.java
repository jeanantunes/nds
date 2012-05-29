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
@Table(name="DNE_GU_UNIDADES_FEDERACAO")
public class UnidadeFederacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1528902275097474122L;

	@Id
	@SequenceGenerator(name="DNE_GU_UNIDADES_FEDERACAO_SIGLA_GENERATOR", sequenceName="DNE_GU_UNIDADES_FEDERACAO_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DNE_GU_UNIDADES_FEDERACAO_SIGLA_GENERATOR")
	@Column(name="SIGLA_UF", unique=true, nullable=false, length=2)
	private String sigla;

	@Column(name="ABREV_UF_REC_ECT", length=36)
	private String abreviatura;

	@Column(name="CHAVE_UF_DNE", nullable=false)
	private Long chaveUf;

	@Column(name="NOME_OFICIAL_UF", length=72)
	private String nome;

	@OneToMany(mappedBy="dneGuUnidadesFederacao")
	private List<FaixaCepUf> faixasCepUfs;

	@OneToMany(mappedBy="dneGuUnidadesFederacao")
	private List<Localidade> localidades;

    @ManyToOne
	@JoinColumn(name="SIGLA_PAIS_1", nullable=false)
	private Pais pais;

    public UnidadeFederacao() {
    }

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
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
	 * @return the chaveUf
	 */
	public Long getChaveUf() {
		return chaveUf;
	}

	/**
	 * @param chaveUf the chaveUf to set
	 */
	public void setChaveUf(Long chaveUf) {
		this.chaveUf = chaveUf;
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
	 * @return the faixasCepUfs
	 */
	public List<FaixaCepUf> getFaixasCepUfs() {
		return faixasCepUfs;
	}

	/**
	 * @param faixasCepUfs the faixasCepUfs to set
	 */
	public void setFaixasCepUfs(List<FaixaCepUf> faixasCepUfs) {
		this.faixasCepUfs = faixasCepUfs;
	}

	/**
	 * @return the localidades
	 */
	public List<Localidade> getLocalidades() {
		return localidades;
	}

	/**
	 * @param localidades the localidades to set
	 */
	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	/**
	 * @return the pais
	 */
	public Pais getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(Pais pais) {
		this.pais = pais;
	}
    
}