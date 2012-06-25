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
@Table(name="LOG_BAIRRO")
public class Bairro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2098584768905693662L;

	@Id
	@Column(name="BAI_NU", unique=true, nullable=false)
	private Long id;

	@Column(name="BAI_NO_ABREV", length=72)
	private String abreviatura;

	@Column(name="BAI_NO", length=144)
	private String nome;

	@Column(name="UFE_SG", length=4)
	private String uf;
	
    @ManyToOne
	@JoinColumn(name="LOC_NU", nullable=false)
	private Localidade localidade;

    public Bairro() {
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
}