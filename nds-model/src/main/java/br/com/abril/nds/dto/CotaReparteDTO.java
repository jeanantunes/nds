package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.Cota;

public class CotaReparteDTO implements Serializable{
	
	private static final long serialVersionUID = -1727632553105891372L;
	
	private Cota cota;
	private boolean cotaContribuinteExigeNF;
	private BigInteger reparte;
	
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
	
}