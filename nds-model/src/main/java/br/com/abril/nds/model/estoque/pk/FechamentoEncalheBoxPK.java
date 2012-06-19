package br.com.abril.nds.model.estoque.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;

@Embeddable
public class FechamentoEncalheBoxPK implements Serializable {

	private static final long serialVersionUID = 7832563570603777070L;

	@ManyToOne(optional = false)
	@JoinColumns({
        @JoinColumn(name="DATA_ENCALHE", referencedColumnName="DATA_ENCALHE"),
        @JoinColumn(name="PRODUTO_EDICAO_ID", referencedColumnName="PRODUTO_EDICAO_ID")
    })
	private FechamentoEncalhe fechamentoEncalhe;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BOX_ID")
	private Box box;


	public FechamentoEncalhe getFechamentoEncalhe() {
		return fechamentoEncalhe;
	}

	public void setFechamentoEncalhe(FechamentoEncalhe fechamentoEncalhe) {
		this.fechamentoEncalhe = fechamentoEncalhe;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((box == null) ? 0 : box.hashCode());
		result = prime
				* result
				+ ((fechamentoEncalhe == null) ? 0 : fechamentoEncalhe
						.hashCode());
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
		FechamentoEncalheBoxPK other = (FechamentoEncalheBoxPK) obj;
		if (box == null) {
			if (other.box != null)
				return false;
		} else if (!box.equals(other.box))
			return false;
		if (fechamentoEncalhe == null) {
			if (other.fechamentoEncalhe != null)
				return false;
		} else if (!fechamentoEncalhe.equals(other.fechamentoEncalhe))
			return false;
		return true;
	}
}
