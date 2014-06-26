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
@DiscriminatorValue(value = "MOVIMENTO_FECHAMENTO_FISCAL_FORNECEDOR")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrigemItemNotaFiscalMovimentoFechamentoFiscalFornecedor extends OrigemItemNotaFiscal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1646731362475540050L;
	
	@XmlTransient
	@Transient
	OrigemItem origem = OrigemItem.MOVIMENTO_FECHAMENTO_FISCAL_FORNECEDOR;
	
	@XmlTransient
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="ORIGEM_ID")
	private MovimentoFechamentoFiscalFornecedor movimentoFechamentoFiscalFornecedor;
	
	public MovimentoFechamentoFiscalFornecedor getMovimentoFechamentoFiscalFornecedor() {
		return movimentoFechamentoFiscalFornecedor;
	}

	public void setMovimentoFechamentoFiscalFornecedor(
			MovimentoFechamentoFiscalFornecedor movimentoFechamentoFiscalFornecedor) {
		this.movimentoFechamentoFiscalFornecedor = movimentoFechamentoFiscalFornecedor;
	}

	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}