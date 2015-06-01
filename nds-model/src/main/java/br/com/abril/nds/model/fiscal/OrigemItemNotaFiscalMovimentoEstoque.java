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

import br.com.abril.nds.model.estoque.MovimentoEstoque;

@Entity
@DiscriminatorValue(value = "MOVIMENTO_ESTOQUE")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrigemItemNotaFiscalMovimentoEstoque extends OrigemItemNotaFiscal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1646731362475540050L;
	
	@XmlTransient
	@Transient
	OrigemItem origem = OrigemItem.MOVIMENTO_ESTOQUE;
	
	@XmlTransient
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="ORIGEM_ID")
	private MovimentoEstoque movimentoEstoque;
	
	public MovimentoEstoque getMovimentoEstoque() {
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