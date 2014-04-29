package br.com.abril.nds.model.fiscal;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import br.com.abril.nds.model.estoque.MovimentoEstoque;

@Entity
@DiscriminatorValue(value = "MOVIMENTO_ESTOQUE_COTA")
public class OrigemItemMovFechamentoFiscalME extends OrigemItemMovFechamentoFiscal {

	private static final long serialVersionUID = -1646731362475540050L;
	
	@Transient
	OrigemItem origem = OrigemItem.MOVIMENTO_ESTOQUE_COTA;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="MOVIMENTO_ESTOQUE_ID")
	private MovimentoEstoque movimentoEstoque;
	
	public MovimentoEstoque getMovimentoEstoqueCota() {
		return movimentoEstoque;
	}

	public void setMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		this.movimentoEstoque = movimentoEstoque;
	}

	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}