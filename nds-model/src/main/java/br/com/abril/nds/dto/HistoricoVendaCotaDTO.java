package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

public class HistoricoVendaCotaDTO implements Serializable {

	private static final long serialVersionUID = -4984021691122691414L;
	
	private Long idCota;
	private BigInteger reparte;
	private BigInteger venda;
	private String statusLancamento;
	public Long getIdCota() {
		return idCota;
	}
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}
	public BigInteger getReparte() {
		return reparte;
	}
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	public BigInteger getVenda() {
		return venda;
	}
	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}
	public String getStatusLancamento() {
		return statusLancamento;
	}
	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
	}
	
}
