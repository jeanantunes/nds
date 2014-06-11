package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

@SuppressWarnings("serial")
public class RateioDiferencaDTO implements Serializable {

	private Integer numeroCota;
	
	private BigInteger qtde;
	
	public RateioDiferencaDTO() {
		
	}
	
	public RateioDiferencaDTO(Integer numeroCota, 
							  BigInteger qtde) {
		
		this.numeroCota = numeroCota;
		this.qtde = qtde;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	
}
