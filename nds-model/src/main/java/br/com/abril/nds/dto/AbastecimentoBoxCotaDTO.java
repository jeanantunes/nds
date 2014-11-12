package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class AbastecimentoBoxCotaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    private BigInteger codigoCota;
    private String nomeProduto;
    private BigInteger numeroEdicao;
    private BigDecimal reparte;
	
    public BigInteger getCodigoCota() {
		return codigoCota;
	}
	
	public void setCodigoCota(BigInteger codigoCota) {
		this.codigoCota = codigoCota;
	}
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public BigInteger getNumeroEdicao() {
		return numeroEdicao;
	}
	
	public void setNumeroEdicao(BigInteger numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}
}