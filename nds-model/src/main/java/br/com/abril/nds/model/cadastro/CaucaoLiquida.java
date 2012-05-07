/**
 * 
 */
package br.com.abril.nds.model.cadastro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;

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
	private Double valor;
	
	@Column(name="DATA_ATUALIZACAO")
	@Temporal(TemporalType.DATE)
	private Date atualizacao;
	
	@Column(name="INDICE_REAJUSTE")
	private Double indiceReajuste;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="COTA_GARANTIA_CAUCAO_LIQUIDA_ID")
	private CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida;
	

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
	public Double getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(Double valor) {
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

	/**
	 * @return the indiceReajuste
	 */
	public Double getIndiceReajuste() {
		return indiceReajuste;
	}

	/**
	 * @param indiceReajuste the indiceReajuste to set
	 */
	public void setIndiceReajuste(Double indiceReajuste) {
		this.indiceReajuste = indiceReajuste;
	}

	/**
	 * @return the cotaGarantiaCaucaoLiquida
	 */
	public CotaGarantiaCaucaoLiquida getCotaGarantiaCaucaoLiquida() {
		return cotaGarantiaCaucaoLiquida;
	}

	/**
	 * @param cotaGarantiaCaucaoLiquida the cotaGarantiaCaucaoLiquida to set
	 */
	public void setCotaGarantiaCaucaoLiquida(
			CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida) {
		this.cotaGarantiaCaucaoLiquida = cotaGarantiaCaucaoLiquida;
	}

}
