package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PDV")
@SequenceGenerator(name="PDV_SEQ", initialValue = 1, allocationSize = 1)
public class PDV implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5121689715572569495L;
	
	@Id
	@GeneratedValue(generator = "PDV_SEQ")
	@Column(name = "ID")
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PDV", nullable = false)
	private TipoPDV tipoPDV;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public TipoPDV getTipoPDV() {
		return tipoPDV;
	}
	
	public void setTipoPDV(TipoPDV tipoPDV) {
		this.tipoPDV = tipoPDV;
	}

}