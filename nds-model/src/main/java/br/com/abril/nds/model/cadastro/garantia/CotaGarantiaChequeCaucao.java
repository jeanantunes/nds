/**
 * 
 */
package br.com.abril.nds.model.cadastro.garantia;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.Cheque;

/**
 * @author Diego Fernandes
 *
 */
@Entity
public class CotaGarantiaChequeCaucao extends CotaGarantia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1237737954973314146L;
		
	@OneToOne(cascade={CascadeType.ALL},orphanRemoval=true)
	@JoinColumn(name="CHEQUE_CAUCAO_ID")
	private Cheque cheque;

	
	/**
	 * @return the cheque
	 */
	public Cheque getCheque() {
		return cheque;
	}


	/**
	 * @param cheque the cheque to set
	 */
	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}
	

}
