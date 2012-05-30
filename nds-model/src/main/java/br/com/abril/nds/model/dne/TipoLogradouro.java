package br.com.abril.nds.model.dne;

import java.io.Serializable;
import javax.persistence.*;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_TIPOS_LOGRADOURO")
public class TipoLogradouro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3479736653601925380L;

	@Id
	@Column(name="CHAVE_TP_LOG_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="ABREV_TP_LOG_REC_ECT", length=15)
	private String abreviatura;

	@Column(name="NOME_OFI_TP_LOG", length=72)
	private String nome;

    public TipoLogradouro() {
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