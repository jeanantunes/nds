package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@MappedSuperclass
public abstract class AssociacaoEndereco {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ENDERECO_ID")
	@Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.MERGE})
	private Endereco endereco;
	
	@Column(name = "TIPO_ENDERECO", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoEndereco tipoEndereco;
	
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}
	
	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}
	
	public boolean isPrincipal() {
		return principal;
	}
	
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssociacaoEndereco other = (AssociacaoEndereco) obj;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		return true;
	}
}
