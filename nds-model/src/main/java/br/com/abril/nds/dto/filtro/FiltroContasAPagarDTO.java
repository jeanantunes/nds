package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

public class FiltroContasAPagarDTO implements Serializable {

	private static final long serialVersionUID = 5604665210912108694L;
	private Date dataDe;
	private Date dataAte;
	private Integer ce;
	private String produto;
	private Long edicao;
	
	
	
	public Date getDataDe() {
		return dataDe;
	}
	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}
	public Date getDataAte() {
		return dataAte;
	}
	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}
	public Integer getCe() {
		return ce;
	}
	public void setCe(Integer ce) {
		this.ce = ce;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	
	
	
	

}
