package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

@Entity
@DiscriminatorValue(value = "MOVIMENTO_ESTOQUE_COTA")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrigemItemMovFechamentoFiscalMEC extends OrigemItemMovFechamentoFiscal {

	private static final long serialVersionUID = -1646731362475540050L;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ORIGEM")
	OrigemItem origem = OrigemItem.MOVIMENTO_ESTOQUE_COTA;

	public OrigemItemMovFechamentoFiscalMEC() {
		super();
	}
	
	public OrigemItemMovFechamentoFiscalMEC(MovimentoFechamentoFiscal mff, MovimentoEstoqueCota mec) {
		super.setMovimento(mec);
		super.setMovimentoFechamentoFiscal(mff);
	}

	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}