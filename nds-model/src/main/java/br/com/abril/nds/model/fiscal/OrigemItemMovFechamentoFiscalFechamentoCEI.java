package br.com.abril.nds.model.fiscal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = "FECHAMENTO_CE_INTEGRACAO")
public class OrigemItemMovFechamentoFiscalFechamentoCEI extends OrigemItemMovFechamentoFiscal {

	private static final long serialVersionUID = -1646731362475540050L;
	
	@Transient
	OrigemItem origem = OrigemItem.FECHAMENTO_CE_INTEGRACAO;
	
	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}