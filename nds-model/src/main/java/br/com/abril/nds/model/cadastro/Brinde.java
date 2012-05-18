package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Classe que possui informações sobre brinde.
 * 
 * @author Discover Technology
 *
 */
@Embeddable
public class Brinde implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -822721180874393822L;

	@Column(name = "DESCRICAO_BRINDE", nullable = true)
	private String descricao;
	
	@Column(name = "VENDE_BRINDE_SEPARADO", nullable = true)
	private Boolean permiteVendaSeparada;
	
	/**
	 * Construtor.
	 */
	public Brinde() {
		
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the permiteVendaSeparada
	 */
	public Boolean getPermiteVendaSeparada() {
		return permiteVendaSeparada;
	}

	/**
	 * @param permiteVendaSeparada the permiteVendaSeparada to set
	 */
	public void setPermiteVendaSeparada(Boolean permiteVendaSeparada) {
		this.permiteVendaSeparada = permiteVendaSeparada;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime
				* result
				+ ((permiteVendaSeparada == null) ? 0 : permiteVendaSeparada
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		Brinde other = (Brinde) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (permiteVendaSeparada == null) {
			if (other.permiteVendaSeparada != null)
				return false;
		} else if (!permiteVendaSeparada.equals(other.permiteVendaSeparada))
			return false;
		return true;
	}
	
}
