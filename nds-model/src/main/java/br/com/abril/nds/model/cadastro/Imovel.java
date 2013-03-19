/**
 * 
 */
package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Diego Fernandes
 *
 */
@Entity
@Table(name="IMOVEL")
@SequenceGenerator(name="IMOVEL_SEQ", initialValue = 1, allocationSize = 1)
public class Imovel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -32116428409247941L;
	
	
	@Id
	@GeneratedValue(generator="IMOVEL_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name="PROPRIETARIO", nullable=false)
	private String proprietario;
	
	@Column(name="ENDERECO", nullable=false)
	private String endereco;
	
	@Column(name="NUMERO_REGISTRO", nullable=false)
	private String numeroRegistro;
	
	@Column(name="VALOR", nullable=false)
	private BigDecimal valor;
	
	@Column(name="OBSERVACAO", nullable=false)
	private String observacao;
	

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
	 * @return the proprietario
	 */
	public String getProprietario() {
		return proprietario;
	}


	/**
	 * @param proprietario the proprietario to set
	 */
	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
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
	 * @return the numeroRegistro
	 */
	public String getNumeroRegistro() {
		return numeroRegistro;
	}


	/**
	 * @param numeroRegistro the numeroRegistro to set
	 */
	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}


	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}


	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}


	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		return observacao;
	}


	/**
	 * @param observacao the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
