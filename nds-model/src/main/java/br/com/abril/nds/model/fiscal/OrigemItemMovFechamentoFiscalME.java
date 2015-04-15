package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import br.com.abril.nds.model.estoque.MovimentoEstoque;

@Entity
@DiscriminatorValue(value = "MOVIMENTO_ESTOQUE")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrigemItemMovFechamentoFiscalME extends OrigemItemMovFechamentoFiscal {

	private static final long serialVersionUID = -1646731362475540050L;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ORIGEM")
	OrigemItem origem = OrigemItem.MOVIMENTO_ESTOQUE;

	public OrigemItemMovFechamentoFiscalME() {
		super();
	}
	
	public OrigemItemMovFechamentoFiscalME(MovimentoFechamentoFiscal mff, MovimentoEstoque me) {
		super.setMovimento(me);
		super.setMovimentoFechamentoFiscal(mff);
	}
	
	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}