package br.com.abril.nds.model.cadastro.garantia;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.NotaPromissoria;

@Entity
public class CotaGarantiaNotaPromissoria extends CotaGarantia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8745281038847495684L;
	
	@OneToOne(cascade={CascadeType.ALL},orphanRemoval=true)
	@JoinColumn(name="NOTA_PROMISSORIA_ID")
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
