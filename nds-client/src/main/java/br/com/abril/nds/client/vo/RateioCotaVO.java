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
	
	private Integer id;
	
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
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
