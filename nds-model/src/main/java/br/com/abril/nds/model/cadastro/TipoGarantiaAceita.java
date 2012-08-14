/**
 * 
 */
package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Diego Fernandes
 *
 */
@Entity
@Table(name = "TIPO_GARANTIA_ACEITA")
@SequenceGenerator(name="TIPO_GARANTIA_ACEITA_SEQ", initialValue = 1, allocationSize = 1)
public class TipoGarantiaAceita implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7309370426863616354L;
	
	@Id
	@GeneratedValue(generator="TIPO_GARANTIA_ACEITA_SEQ")
	@Column
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoGarantia tipoGarantia;

	@Column(name = "VALOR", nullable = true)
	private Integer valor;
	
	@Column(name = "UTILIZA", nullable = true)
	private boolean utilizar;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public TipoGarantia getTipoGarantia() {
		return tipoGarantia;
	}



	public void setTipoGarantia(TipoGarantia tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}



	public Distribuidor getDistribuidor() {
		return distribuidor;
	}



	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}



	/**
	 * 
	 */
	public TipoGarantiaAceita() {
	}



	public Integer getValor() {
		return valor;
	}



	public void setValor(Integer valor) {
		this.valor = valor;
	}



	public boolean isUtilizar() {
		return utilizar;
	}



	public void setUtilizar(boolean utilizar) {
		this.utilizar = utilizar;
	}
	
	
}
