package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

public class FiltroConsultaNegociacoesDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -3322353136192094144L;
	
	private Integer numeroCota;
	private String situacaoParcela;
	private Date dataNegociacaoDe;
	private Date dataNegociacaoAte;
	private Date dataVencimentoDe;
	private Date dataVencimentoAte;
	
	
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getSituacaoParcela() {
		return situacaoParcela;
	}
	public void setSituacaoParcela(String situacaoParcela) {
		this.situacaoParcela = situacaoParcela;
	}
	public Date getDataNegociacaoDe() {
		return dataNegociacaoDe;
	}
	public void setDataNegociacaoDe(Date dataNegociacaoDe) {
		this.dataNegociacaoDe = dataNegociacaoDe;
	}
	public Date getDataNegociacaoAte() {
		return dataNegociacaoAte;
	}
	public void setDataNegociacaoAte(Date dataNegociacaoAte) {
		this.dataNegociacaoAte = dataNegociacaoAte;
	}
	public Date getDataVencimentoDe() {
		return dataVencimentoDe;
	}
	public void setDataVencimentoDe(Date dataVencimentoDe) {
		this.dataVencimentoDe = dataVencimentoDe;
	}
	public Date getDataVencimentoAte() {
		return dataVencimentoAte;
	}
	public void setDataVencimentoAte(Date dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}

}
