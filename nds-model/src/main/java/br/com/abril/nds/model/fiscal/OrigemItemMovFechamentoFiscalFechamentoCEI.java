package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue(value = "MOVIMENTO_ESTOQUE")
public class OrigemItemMovFechamentoFiscalFechamentoCEI extends OrigemItemMovFechamentoFiscal {

	private static final long serialVersionUID = -1646731362475540050L;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ORIGEM")
	OrigemItem origem = OrigemItem.FECHAMENTO_CE_INTEGRACAO;

	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}