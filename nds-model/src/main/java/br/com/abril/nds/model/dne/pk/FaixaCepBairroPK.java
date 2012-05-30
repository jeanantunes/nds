package br.com.abril.nds.model.dne.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * @author Discover Technology
 *
 */
@Embeddable
public class FaixaCepBairroPK implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4537316275840182349L;

	@Column(name="CHAVE_BAI_DNE", unique=true, nullable=false)
	private String chaveBairro;

	@Column(name="ORDEM_PRI_FX_CEP_REG", unique=true, nullable=false)
	private String ordemFaixaCep;

    public FaixaCepBairroPK() {
    }

	/**
	 * @return the chaveBairro
	 */
	public String getChaveBairro() {
		return chaveBairro;
	}

	/**
	 * @param chaveBairro the chaveBairro to set
	 */
	public void setChaveBairro(String chaveBairro) {
		this.chaveBairro = chaveBairro;
	}

	/**
	 * @return the ordemFaixaCep
	 */
	public String getOrdemFaixaCep() {
		return ordemFaixaCep;
	}

	/**
	 * @param ordemFaixaCep the ordemFaixaCep to set
	 */
	public void setOrdemFaixaCep(String ordemFaixaCep) {
		this.ordemFaixaCep = ordemFaixaCep;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chaveBairro == null) ? 0 : chaveBairro.hashCode());
		result = prime * result
				+ ((ordemFaixaCep == null) ? 0 : ordemFaixaCep.hashCode());
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
		FaixaCepBairroPK other = (FaixaCepBairroPK) obj;
		if (chaveBairro == null) {
			if (other.chaveBairro != null)
				return false;
		} else if (!chaveBairro.equals(other.chaveBairro))
			return false;
		if (ordemFaixaCep == null) {
			if (other.ordemFaixaCep != null)
				return false;
		} else if (!ordemFaixaCep.equals(other.ordemFaixaCep))
			return false;
		return true;
	}
	
	
}