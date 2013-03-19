/**
 * 
 */
package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name="NOTA_PROMISSORIA")
@SequenceGenerator(name="NOTA_PROMISSORIA_SEQ",initialValue=1,allocationSize=1)
public class NotaPromissoria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4395633014152872902L;
	
	@Id
	@GeneratedValue(generator="NOTA_PROMISSORIA_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="VENCIMENTO", nullable=false)
	private Date vencimento;
	
	@Column(name="VALOR", nullable=false)
	private BigDecimal valor;
	
	@Column(name="VALOR_EXTENSO", nullable=false)
	private String valorExtenso;

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
	 * @return the vencimento
	 */
	public Date getVencimento() {
		return vencimento;
	}

	/**
	 * @param vencimento the vencimento to set
	 */
	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
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
	 * @return the valorExtenso
	 */
	public String getValorExtenso() {
		return valorExtenso;
	}

	/**
	 * @param valorExtenso the valorExtenso to set
	 */
	public void setValorExtenso(String valorExtenso) {
		this.valorExtenso = valorExtenso;
	}

}
