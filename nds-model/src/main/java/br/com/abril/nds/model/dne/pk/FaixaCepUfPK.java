package br.com.abril.nds.model.dne.pk;

import java.io.Serializable;
import javax.persistence.*;


/**
 * @author Discover Technology
 *
 */
@Embeddable
public class FaixaCepUfPK implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4445082219949986538L;

	@Column(name="ORDEM_PRI_FX_CEP_REG", unique=true, nullable=false)
	private String ordemFaixaCep;

	@Column(name="SIGLA_UF", unique=true, nullable=false, length=2)
	private String siglaUf;

    public FaixaCepUfPK() {
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

	/**
	 * @return the siglaUf
	 */
	public String getSiglaUf() {
		return siglaUf;
	}

	/**
	 * @param siglaUf the siglaUf to set
	 */
	public void setSiglaUf(String siglaUf) {
		this.siglaUf = siglaUf;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ordemFaixaCep == null) ? 0 : ordemFaixaCep.hashCode());
		result = prime * result + ((siglaUf == null) ? 0 : siglaUf.hashCode());
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
		FaixaCepUfPK other = (FaixaCepUfPK) obj;
		if (ordemFaixaCep == null) {
			if (other.ordemFaixaCep != null)
				return false;
		} else if (!ordemFaixaCep.equals(other.ordemFaixaCep))
			return false;
		if (siglaUf == null) {
			if (other.siglaUf != null)
				return false;
		} else if (!siglaUf.equals(other.siglaUf))
			return false;
		return true;
	}
	
}