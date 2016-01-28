package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.Cota;

public class CotaReparteProdutoDTO implements Serializable{
	
	private static final long serialVersionUID = -1727632553105891372L;
	
	private Integer numeroCota;
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	private boolean cotaContribuinteExigeNF;
	private Long reparte;
	
	private Long encalhe;
	
	
	private Long idBox;
	
	private String nomeBox;
	
	private String nomeCota;
	
	private boolean parcialFinal=false;
	
	
	
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
	public Long getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(Long reparte) {
		this.reparte = reparte;
	}

	
	public boolean isParcialFinal() {
		return parcialFinal;
	}
	public void setParcialFinal(boolean parcialFinal) {
		this.parcialFinal = parcialFinal;
	}
	
	public String getNomeBox() {
		return nomeBox;
	}
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}
	public Long getIdBox() {
		return idBox;
	}
	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}
	

	
	public String getNomeCota() {
		return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	

	public Long getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(Long encalhe) {
		this.encalhe = encalhe;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroCota == null) ? 0 : numeroCota.hashCode());
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
		CotaReparteProdutoDTO other = (CotaReparteProdutoDTO) obj;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		return true;
	}
}