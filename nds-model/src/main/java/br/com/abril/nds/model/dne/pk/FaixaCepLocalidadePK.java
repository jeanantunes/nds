package br.com.abril.nds.model.dne.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * @author Discover Technology
 *
 */
@Embeddable
public class FaixaCepLocalidadePK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3967268689971183330L;

	@Column(name="CHAVE_LOC_DNE", unique=true, nullable=false)
	private String chaveLocalidade;

	@Column(name="ORDEM_PRI_FX_CEP_REG", unique=true, nullable=false)
	private String ordemFaixaCep;

    public FaixaCepLocalidadePK() {
    }

	/**
	 * @return the chaveLocalidade
	 */
	public String getChaveLocalidade() {
		return chaveLocalidade;
	}

	/**
	 * @param chaveLocalidade the chaveLocalidade to set
	 */
	public void setChaveLocalidade(String chaveLocalidade) {
		this.chaveLocalidade = chaveLocalidade;
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
				+ ((chaveLocalidade == null) ? 0 : chaveLocalidade.hashCode());
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
		FaixaCepLocalidadePK other = (FaixaCepLocalidadePK) obj;
		if (chaveLocalidade == null) {
			if (other.chaveLocalidade != null)
				return false;
		} else if (!chaveLocalidade.equals(other.chaveLocalidade))
			return false;
		if (ordemFaixaCep == null) {
			if (other.ordemFaixaCep != null)
				return false;
		} else if (!ordemFaixaCep.equals(other.ordemFaixaCep))
			return false;
		return true;
	}
	
}