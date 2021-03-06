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
@Table(name = "NOTA_FISCAL_ORIGEM_ITEM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class OrigemItemNotaFiscal implements Serializable {
	
	private static final long serialVersionUID = 4968512055654760321L;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrigemItemNotaFiscal other = (OrigemItemNotaFiscal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}