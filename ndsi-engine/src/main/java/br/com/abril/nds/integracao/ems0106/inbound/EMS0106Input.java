package br.com.abril.nds.integracao.ems0106.inbound;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0106Input implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codigoPublicacao;
	private Long edicao;
	private Long reparteDistribuir;

	@Field(offset = 1, length = 8)
	public String getCodigoPublicacao() {
		return codigoPublicacao;
	}

	public void setCodigoPublicacao(String codigoPublicacao) {
		this.codigoPublicacao = codigoPublicacao;
	}

	@Field(offset = 9, length = 4)
	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	@Field(offset = 13, length = 8)
	@FixedFormatDecimal(decimals=2, useDecimalDelimiter=false)
	public Long getReparteDistribuir() {
		return reparteDistribuir;
	}

	public void setReparteDistribuir(Long reparteDistribuir) {
		this.reparteDistribuir = reparteDistribuir;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codigoPublicacao == null) ? 0 : codigoPublicacao.hashCode());
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
		result = prime
				* result
				+ ((reparteDistribuir == null) ? 0 : reparteDistribuir
						.hashCode());
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
		EMS0106Input other = (EMS0106Input) obj;
		if (codigoPublicacao == null) {
			if (other.codigoPublicacao != null)
				return false;
		} else if (!codigoPublicacao.equals(other.codigoPublicacao))
			return false;
		if (edicao == null) {
			if (other.edicao != null)
				return false;
		} else if (!edicao.equals(other.edicao))
			return false;
		if (reparteDistribuir == null) {
			if (other.reparteDistribuir != null)
				return false;
		} else if (!reparteDistribuir.equals(other.reparteDistribuir))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EMS0106Input [codigoPublicacao=" + codigoPublicacao
				+ ", edicao=" + edicao + ", reparteDistribuir="
				+ reparteDistribuir + "]";
	}
}
