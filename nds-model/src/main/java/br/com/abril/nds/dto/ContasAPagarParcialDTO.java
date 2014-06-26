package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ContasAPagarParcialDTO implements Serializable {

	private static final long serialVersionUID = -8886274959259550126L;
	
	private Date lcto;
	private Date rclt;
	private BigInteger reparte;
	private BigInteger suplementacao;
	private BigInteger encalhe;
	private BigInteger venda;
	private BigDecimal pctVenda;
	private BigInteger vendaCe;
	private BigInteger reparteAcum;
	private BigInteger vendaAcum;
	private BigDecimal pctVendaAcum;
	private String nfe;
	private Long idLancamento;
	
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
	public BigInteger getReparte() {
		return reparte;
	}
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	public BigInteger getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(BigInteger suplementacao) {
		this.suplementacao = suplementacao;
	}
	public BigInteger getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}
	public BigInteger getVenda() {
		return venda;
	}
	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}
	public BigDecimal getPctVenda() {
		return pctVenda;
	}
	public void setPctVenda(BigDecimal pctVenda) {
		this.pctVenda = pctVenda;
	}
	public BigInteger getVendaCe() {
		return vendaCe;
	}
	public void setVendaCe(BigInteger vendaCe) {
		this.vendaCe = vendaCe;
	}
	public BigInteger getReparteAcum() {
		return reparteAcum;
	}
	public void setReparteAcum(BigInteger reparteAcum) {
		this.reparteAcum = reparteAcum;
	}
	public BigInteger getVendaAcum() {
		return vendaAcum;
	}
	public void setVendaAcum(BigInteger vendaAcum) {
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
	public Long getIdLancamento() {
		return idLancamento;
	}
	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}
}
