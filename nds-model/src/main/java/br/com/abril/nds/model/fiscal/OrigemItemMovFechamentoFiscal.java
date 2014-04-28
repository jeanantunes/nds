package br.com.abril.nds.model.fiscal;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL_ORIGEM_ITEM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public class OrigemItemMovFechamentoFiscal implements Serializable {

	private static final long serialVersionUID = 5063560705747832645L;

	@Id
	@GeneratedValue
	private Long id;

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