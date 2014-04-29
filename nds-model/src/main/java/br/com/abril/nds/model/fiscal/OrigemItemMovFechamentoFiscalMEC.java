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

import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

@Entity
@DiscriminatorValue(value = "MOVIMENTO_ESTOQUE_COTA")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrigemItemMovFechamentoFiscalMEC extends OrigemItemMovFechamentoFiscal {

	private static final long serialVersionUID = -1646731362475540050L;
	
	@XmlTransient
	@Transient
	OrigemItem origem = OrigemItem.MOVIMENTO_ESTOQUE_COTA;
	
	@XmlTransient
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="MOVIMENTO_ESTOQUE_ID")
	private MovimentoEstoqueCota movimentoEstoqueCota;
	
	public OrigemItemMovFechamentoFiscalMEC(MovimentoEstoqueCota mec) {
		this.movimentoEstoqueCota = mec;
	}
	
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