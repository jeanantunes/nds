/**
 * 
 */
package br.com.abril.nds.model.cadastro.garantia;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Fiador;

/**
 * @author Diego Fernandes
 *
 */
@Entity
public class CotaGarantiaFiador extends CotaGarantia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8954847105572780016L;
	
	
	@ManyToOne(optional=true,fetch=FetchType.EAGER, cascade=CascadeType.DETACH)
	@JoinColumn(name="FIADOR_ID")
	private Fiador fiador;


	/**
	 * @return the fiador
	 */
	public Fiador getFiador() {
		return fiador;
	}


	/**
	 * @param fiador the fiador to set
	 */
	public void setFiador(Fiador fiador) {
		this.fiador = fiador;
	}

}
