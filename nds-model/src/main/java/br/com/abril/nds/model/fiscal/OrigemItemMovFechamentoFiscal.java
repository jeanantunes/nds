package br.com.abril.nds.model.fiscal;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.movimentacao.Movimento;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL_ORIGEM_ITEM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class OrigemItemMovFechamentoFiscal implements Serializable {

	private static final long serialVersionUID = 5063560705747832645L;

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="MOVIMENTO_ID")
	private Movimento movimento;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="MOVIMENTO_FECHAMENTO_FISCAL_ID")
	private MovimentoFechamentoFiscal movimentoFechamentoFiscal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Movimento getMovimento() {
		return movimento;
	}

	public void setMovimento(Movimento movimento) {
		this.movimento = movimento;
	}

	public MovimentoFechamentoFiscal getMovimentoFechamentoFiscal() {
		return movimentoFechamentoFiscal;
	}

	public void setMovimentoFechamentoFiscal(
			MovimentoFechamentoFiscal movimentoFechamentoFiscal) {
		this.movimentoFechamentoFiscal = movimentoFechamentoFiscal;
	}

	public String obterOrigemItemNotaFiscal() {
		return null;
	};

	public OrigemItem getOrigem() {
		return null;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}