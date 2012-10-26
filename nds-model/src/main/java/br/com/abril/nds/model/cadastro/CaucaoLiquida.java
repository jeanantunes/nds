/**
 * 
 */
package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Diego Fernandes
 *
 */

@Entity
@Table(name="CAUCAO_LIQUIDA")
@SequenceGenerator(name="CAUCAO_LIQUIDA_SEQ", allocationSize=1,initialValue=1)
public class CaucaoLiquida implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1893954731831702479L;
	
	@Id
	@GeneratedValue(generator="CAUCAO_LIQUIDA_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name="VALOR")
	private BigDecimal valor;
	
	@Column(name="DATA_ATUALIZACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date atualizacao;
	
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
	 * @return the atualizacao
	 */
	public Date getAtualizacao() {
		return atualizacao;
	}

	/**
	 * @param atualizacao the atualizacao to set
	 */
	public void setAtualizacao(Date atualizacao) {
		this.atualizacao = atualizacao;
	}

}
