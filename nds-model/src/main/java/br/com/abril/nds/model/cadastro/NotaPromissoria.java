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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;

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
	
	@Temporal(TemporalType.DATE)
	@Column(name="VENCIMENTO")
	private Date vencimento;
	
	@Column(name="VALOR")
	private Double valor;
	
	@Column(name="VALOR_EXTENSO")
	private String valorExtenso;
	
	@OneToOne(optional=false)
	@JoinColumn(name="COTA_GARANTIA_NOTA_PROMISSORIA_ID")
	private CotaGarantiaNotaPromissoria cotaGarantiaNotaPromissoria;

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

	
	/**
	 * @return the cotaGarantiaNotaPromissoria
	 */
	public CotaGarantiaNotaPromissoria getCotaGarantiaNotaPromissoria() {
		return cotaGarantiaNotaPromissoria;
	}

	/**
	 * @param cotaGarantiaNotaPromissoria the cotaGarantiaNotaPromissoria to set
	 */
	public void setCotaGarantiaNotaPromissoria(
			CotaGarantiaNotaPromissoria cotaGarantiaNotaPromissoria) {
		this.cotaGarantiaNotaPromissoria = cotaGarantiaNotaPromissoria;
	}
	
	
	
	

}
