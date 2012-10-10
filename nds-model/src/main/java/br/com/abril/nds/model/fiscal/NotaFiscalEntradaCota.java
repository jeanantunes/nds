package br.com.abril.nds.model.fiscal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;

@Entity
@DiscriminatorValue(value = "COTA")
public class NotaFiscalEntradaCota extends NotaFiscalEntrada {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne
	@JoinColumn(name = "NOTA_FISCAL_ENTRADA_ID", referencedColumnName="ID", insertable = false, updatable = false)
	private ControleConferenciaEncalheCota controleConferenciaEncalheCota;
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}

	/**
	 * @return the controleConferenciaEncalheCota
	 */
	public ControleConferenciaEncalheCota getControleConferenciaEncalheCota() {
		return controleConferenciaEncalheCota;
	}

	/**
	 * @param controleConferenciaEncalheCota the controleConferenciaEncalheCota to set
	 */
	public void setControleConferenciaEncalheCota(
			ControleConferenciaEncalheCota controleConferenciaEncalheCota) {
		this.controleConferenciaEncalheCota = controleConferenciaEncalheCota;
	}


}