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
@Table(name="DNE_GU_LOGRADOUROS_COMPL1")
public class Complemento implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7468101631962909249L;

	@Id
	@Column(name="CHAVE_COMPLE_1_DNE", unique=true, nullable=false)
	private String id;

	@Column(name="CEP_COMPL1", length=8)
	private String cep;

	@Column(name="NOME_COMPLE_1", length=36)
	private String nome;

	@Column(name="NUM_LETRA_COMPLE_1", length=11)
	private String numero;

	@Column(name="NUM_LOT_DNE", length=11)
	private String numeroLote;

    @ManyToOne
	@JoinColumn(name="CHAVE_LOT_DNE")
	private Lote lote;

    public Complemento() {
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
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
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
	 * @return the lote
	 */
	public Lote getLote() {
		return lote;
	}

	/**
	 * @param lote the lote to set
	 */
	public void setLote(Lote lote) {
		this.lote = lote;
	}

}