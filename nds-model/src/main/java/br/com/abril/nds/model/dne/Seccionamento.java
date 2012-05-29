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
@Table(name="DNE_GU_LOGRADOUROS_SEC")
public class Seccionamento implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6471398752412706359L;

	@Id
	@Column(name="CHAVE_SECC_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="CEP_SEC", length=8)
	private String cep;

	@Column(name="IDENT_PARIDADE", length=1)
	private String identificacaoParidade;

	@Column(name="NUM_FIM_TRECHO")
	private Long numeroFinalTrecho;

	@Column(name="NUM_INI_TRECHO")
	private Long numeroInicialTrecho;
	
    @ManyToOne
	@JoinColumn(name="CHAVE_LOGRADOURO_DNE", nullable=false)
	private Logradouro logradouro;

    public Seccionamento() {
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
	 * @return the identificacaoParidade
	 */
	public String getIdentificacaoParidade() {
		return identificacaoParidade;
	}

	/**
	 * @param identificacaoParidade the identificacaoParidade to set
	 */
	public void setIdentificacaoParidade(String identificacaoParidade) {
		this.identificacaoParidade = identificacaoParidade;
	}

	/**
	 * @return the numeroFinalTrecho
	 */
	public Long getNumeroFinalTrecho() {
		return numeroFinalTrecho;
	}

	/**
	 * @param numeroFinalTrecho the numeroFinalTrecho to set
	 */
	public void setNumeroFinalTrecho(Long numeroFinalTrecho) {
		this.numeroFinalTrecho = numeroFinalTrecho;
	}

	/**
	 * @return the numeroInicialTrecho
	 */
	public Long getNumeroInicialTrecho() {
		return numeroInicialTrecho;
	}

	/**
	 * @param numeroInicialTrecho the numeroInicialTrecho to set
	 */
	public void setNumeroInicialTrecho(Long numeroInicialTrecho) {
		this.numeroInicialTrecho = numeroInicialTrecho;
	}

	/**
	 * @return the logradouro
	 */
	public Logradouro getLogradouro() {
		return logradouro;
	}

	/**
	 * @param logradouro the logradouro to set
	 */
	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

}