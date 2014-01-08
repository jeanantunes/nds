package br.com.abril.nds.model.fiscal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

@Entity
@DiscriminatorValue(value = "MOVIMENTO_ESTOQUE_COTA")
public class OrigemItemNotaFiscalMovimentoEstoqueCota extends OrigemItemNotaFiscal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1646731362475540050L;
	
	@Transient
	OrigemItem origem = OrigemItem.MOVIMENTO_ESTOQUE_COTA;
	
	@OneToOne
	@JoinColumn(name="MOVIMENTO_ESTOQUE_COTA_ID")
	private MovimentoEstoqueCota movimentoEstoqueCota;
	
	public MovimentoEstoqueCota getMovimentoEstoqueCota() {
		return movimentoEstoqueCota;
	}

	public void setMovimentoEstoqueCota(MovimentoEstoqueCota movimentoEstoqueCota) {
		this.movimentoEstoqueCota = movimentoEstoqueCota;
	}

	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}