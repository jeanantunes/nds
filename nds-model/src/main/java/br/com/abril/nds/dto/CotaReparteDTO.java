package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.Cota;

public class CotaReparteDTO implements Serializable{
	
	private static final long serialVersionUID = -1727632553105891372L;
	
	private Cota cota;
	private BigInteger reparte;
	private Long idLancamento;
	
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
}