package br.com.abril.nds.model.cadastro.garantia;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.NotaPromissoria;

@Entity
@Table(name="COTA_GARANTIA_NOTA_PROMISSORIA")
public class CotaGarantiaNotaPromissoria extends CotaGarantia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8745281038847495684L;
	
	@OneToOne(mappedBy="cotaGarantiaNotaPromissoria",cascade={CascadeType.ALL})
	private NotaPromissoria notaPromissoria;

	/**
	 * @return the notaPromissoria
	 */
	public NotaPromissoria getNotaPromissoria() {
		return notaPromissoria;
	}

	/**
	 * @param notaPromissoria the notaPromissoria to set
	 */
	public void setNotaPromissoria(NotaPromissoria notaPromissoria) {
		this.notaPromissoria = notaPromissoria;
	}

}
