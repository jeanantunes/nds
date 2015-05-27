package br.com.abril.nds.model.fiscal;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@DiscriminatorValue(value = "MOVIMENTO_FECHAMENTO_FISCAL_COTA")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrigemItemNotaFiscalMovimentoFechamentoFiscalCota extends OrigemItemNotaFiscal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1646731362475540050L;
	
	@XmlTransient
	@Transient
	OrigemItem origem = OrigemItem.MOVIMENTO_FECHAMENTO_FISCAL_COTA;
	
	@XmlTransient
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="ORIGEM_ID")
	private MovimentoFechamentoFiscalCota movimentoFechamentoFiscalCota;
	
	public MovimentoFechamentoFiscalCota getMovimentoFechamentoFiscalCota() {
		return movimentoFechamentoFiscalCota;
	}

	public void setMovimentoFechamentoFiscalCota(MovimentoFechamentoFiscalCota movimentoFechamentoFiscalCota) {
		this.movimentoFechamentoFiscalCota = movimentoFechamentoFiscalCota;
	}

	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}