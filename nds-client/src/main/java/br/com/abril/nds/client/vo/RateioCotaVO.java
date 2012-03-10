package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value Object para rateio de cotas.
 * 
 * @author Discover Technology
 *
 */
public class RateioCotaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3244681304919095515L;
	
	private Long idDiferenca;
	
	private Integer numeroCota;
	
	private BigDecimal reparteCota;
	
	private BigDecimal quantidade;
	
	/**
	 * Construtor padrão.
	 */
	public RateioCotaVO() {
		
	}

	/**
	 * @return the idDiferenca
	 */
	public Long getIdDiferenca() {
		return idDiferenca;
	}

	/**
	 * @param idDiferenca the idDiferenca to set
	 */
	public void setIdDiferenca(Long idDiferenca) {
		this.idDiferenca = idDiferenca;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the reparteCota
	 */
	public BigDecimal getReparteCota() {
		return reparteCota;
	}

	/**
	 * @param reparteCota the reparteCota to set
	 */
	public void setReparteCota(BigDecimal reparteCota) {
		this.reparteCota = reparteCota;
	}

	/**
	 * @return the quantidade
	 */
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idDiferenca == null) ? 0 : idDiferenca.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
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
		RateioCotaVO other = (RateioCotaVO) obj;
		if (idDiferenca == null) {
			if (other.idDiferenca != null)
				return false;
		} else if (!idDiferenca.equals(other.idDiferenca))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		return true;
	}

}
