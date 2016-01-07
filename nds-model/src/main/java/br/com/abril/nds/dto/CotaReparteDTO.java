package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.Cota;

public class CotaReparteDTO implements Serializable{
	
	private static final long serialVersionUID = -1727632553105891372L;
	
	private Cota cota;
	private boolean cotaContribuinteExigeNF;
	private BigInteger reparte;
	private Long idLancamento;
	private boolean parcialFinal=false;
	
	
	/**
	 * @return the cota
	 */
	public Cota getCota() {
		return cota;
	}
	/**
	 * @param cota the cota to set
	 */
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	/**
	 * @return
	 */
	public boolean isCotaContribuinteExigeNF() {
		return cotaContribuinteExigeNF;
	}
	/**
	 * @param cotaContribuinteExigeNF
	 */
	public void setCotaContribuinteExigeNF(boolean cotaContribuinteExigeNF) {
		this.cotaContribuinteExigeNF = cotaContribuinteExigeNF;
	}
	
	
	/**
	 * @return the reparte
	 */
	public BigInteger getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	/**
	 * @return the idLancamento
	 */
	public Long getIdLancamento() {
		return idLancamento;
	}
	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}	
	
	public boolean isParcialFinal() {
		return parcialFinal;
	}
	public void setParcialFinal(boolean parcialFinal) {
		this.parcialFinal = parcialFinal;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cota == null) ? 0 : cota.getId().hashCode());
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
		CotaReparteDTO other = (CotaReparteDTO) obj;
		if (cota == null) {
			if (other.cota != null)
				return false;
		} else if (!cota.getId().equals(other.cota.getId()))
			return false;
		return true;
	}
}