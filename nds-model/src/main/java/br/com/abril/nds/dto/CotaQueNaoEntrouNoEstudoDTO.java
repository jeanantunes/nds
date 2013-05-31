package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class CotaQueNaoEntrouNoEstudoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long numeroCota;
	private String nomeCota;
	private String motivo;
	private String descricaoMotivo;
	private BigInteger quantidade;
	
	public Long getNumeroCota() {
	    return numeroCota;
	}
	public void setNumeroCota(Long numeroCota) {
	    this.numeroCota = numeroCota;
	}
	public String getNomeCota() {
	    return nomeCota;
	}
	public void setNomeCota(String nomeCota) {
	    this.nomeCota = nomeCota;
	}
	public String getMotivo() {
	    return motivo;
	}
	public void setMotivo(String motivo) {
	    this.motivo = motivo;
	}
	public BigInteger getQuantidade() {
	    return quantidade;
	}
	public void setQuantidade(BigInteger quantidade) {
	    this.quantidade = quantidade;
	}

    public String getDescricaoMotivo() {
        return descricaoMotivo;
    }

    public void setDescricaoMotivo(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }
}
