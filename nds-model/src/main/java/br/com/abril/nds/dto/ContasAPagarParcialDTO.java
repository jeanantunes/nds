package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContasAPagarParcialDTO implements Serializable {

	private static final long serialVersionUID = -8886274959259550126L;
	
	private Date lcto;
	private Date rclt;
	private Integer reparte;
	private Integer suplementacao;
	private Integer encalhe;
	private Integer venda;
	private BigDecimal pctVenda;
	private Integer vendaCe;
	private Integer reparteAcum;
	private Integer vendaAcum;
	private BigDecimal pctVendaAcum;
	private String nfe;
	
	
	public Date getLcto() {
		return lcto;
	}
	public void setLcto(Date lcto) {
		this.lcto = lcto;
	}
	public Date getRclt() {
		return rclt;
	}
	public void setRclt(Date rclt) {
		this.rclt = rclt;
	}
	public Integer getReparte() {
		return reparte;
	}
	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}
	public Integer getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(Integer suplementacao) {
		this.suplementacao = suplementacao;
	}
	public Integer getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(Integer encalhe) {
		this.encalhe = encalhe;
	}
	public Integer getVenda() {
		return venda;
	}
	public void setVenda(Integer venda) {
		this.venda = venda;
	}
	public BigDecimal getPctVenda() {
		return pctVenda;
	}
	public void setPctVenda(BigDecimal pctVenda) {
		this.pctVenda = pctVenda;
	}
	public Integer getVendaCe() {
		return vendaCe;
	}
	public void setVendaCe(Integer vendaCe) {
		this.vendaCe = vendaCe;
	}
	public Integer getReparteAcum() {
		return reparteAcum;
	}
	public void setReparteAcum(Integer reparteAcum) {
		this.reparteAcum = reparteAcum;
	}
	public Integer getVendaAcum() {
		return vendaAcum;
	}
	public void setVendaAcum(Integer vendaAcum) {
		this.vendaAcum = vendaAcum;
	}
	public BigDecimal getPctVendaAcum() {
		return pctVendaAcum;
	}
	public void setPctVendaAcum(BigDecimal pctVendaAcum) {
		this.pctVendaAcum = pctVendaAcum;
	}
	public String getNfe() {
		return nfe;
	}
	public void setNfe(String nfe) {
		this.nfe = nfe;
	}
}
