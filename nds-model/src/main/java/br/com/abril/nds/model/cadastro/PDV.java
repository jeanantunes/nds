package br.com.abril.nds.model.cadastro;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PDV")
public class PDV {

	@Id
	private Long id;
	@Enumerated(EnumType.STRING)
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