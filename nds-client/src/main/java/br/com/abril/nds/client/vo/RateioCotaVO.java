package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

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
	
	private Long id;
	
	private Long idRateio;
	
	private Long idDiferenca;
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private BigInteger reparteCota;
	
	private BigInteger quantidade;
	
	private BigInteger reparteAtualCota;
	
	/**
	 * Construtor padrão.
	 */
	public RateioCotaVO() {
		
	}
	
	
	
	/**
	 * @return the idRateio
	 */
	public Long getIdRateio() {
		return idRateio;
	}



	/**
	 * @param idRateio the idRateio to set
	 */
	public void setIdRateio(Long idRateio) {
		this.idRateio = idRateio;
	}



	/**
	 * @return the reparteAtualCota
	 */
	public BigInteger getReparteAtualCota() {
		return reparteAtualCota;
	}



	/**
	 * @param reparteAtualCota the reparteAtualCota to set
	 */
	public void setReparteAtualCota(BigInteger reparteAtualCota) {
		this.reparteAtualCota = reparteAtualCota;
	}



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
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
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the reparteCota
	 */
	public BigInteger getReparteCota() {
		return reparteCota;
	}

	/**
	 * @param reparteCota the reparteCota to set
	 */
	public void setReparteCota(BigInteger reparteCota) {
		this.reparteCota = reparteCota;
	}

	/**
	 * @return the quantidade
	 */
	public BigInteger getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigInteger quantidade) {
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
		result = prime * result
				+ ((idDiferenca == null) ? 0 : idDiferenca.hashCode());
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result
				+ ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result
				+ ((reparteCota == null) ? 0 : reparteCota.hashCode());
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
		if (idDiferenca == null) {
			if (other.idDiferenca != null)
				return false;
		} else if (!idDiferenca.equals(other.idDiferenca))
			return false;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (reparteCota == null) {
			if (other.reparteCota != null)
				return false;
		} else if (!reparteCota.equals(other.reparteCota))
			return false;
		return true;
	}

}
