package br.com.abril.nds.model.estudo;

import java.io.Serializable;
import java.math.BigInteger;

@SuppressWarnings("serial")
public class CotaLiberacaoEstudo implements Serializable {

	private Long numeroCota;
	
	private BigInteger reparte;
	
	public CotaLiberacaoEstudo() {
		
	}

	public CotaLiberacaoEstudo(Long numeroCota, BigInteger reparte) {
		
		this.numeroCota = numeroCota;
		this.reparte = reparte;
	}

	public Long getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Long numeroCota) {
		this.numeroCota = numeroCota;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
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
		CotaLiberacaoEstudo other = (CotaLiberacaoEstudo) obj;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		return true;
	}
	
}
