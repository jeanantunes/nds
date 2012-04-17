package br.com.abril.nds.model.fiscal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Cota;

@Entity
@DiscriminatorValue(value = "COTA")
public class NotaFiscalEntradaCota extends NotaFiscalEntrada {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}


}