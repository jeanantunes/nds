package br.com.abril.nds.model.dne;

import java.io.Serializable;
import javax.persistence.*;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_TITULOS_PATENTES")
public class TituloPatente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6071629238653956307L;

	@Id
	@Column(name="CHAVE_TIT_PAT_LOG_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="ABREV_TIT_REC_ECT", length=15)
	private String abreviatura;

	@Column(name="NOME_OFI_TIT_PAT", length=72)
	private String nome;

    public TituloPatente() {
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

}