package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BaixaBancariaConsolidadaDTO {
	private Integer identificadorLinha;
	private Date dataBaixa;
	private String destino;
	private Integer codJornaleiro;
	private Date dataVencimento;
	private Date dataPagamento;
	private BigDecimal valorDoBoleto;
	private BigDecimal valorPago;
	private String nossoNumeroConsolidado;
	private Integer qtdRegistros;
	
	
	public Integer getIdentificadorLinha() {
		return identificadorLinha;
	}
	public void setIdentificadorLinha(Integer identificadorLinha) {
		this.identificadorLinha = identificadorLinha;
	}
	public Date getDataBaixa() {
		return dataBaixa;
	}
	public void setDataBaixa(Date dataBaixa) {
		this.dataBaixa = dataBaixa;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Integer getCodJornaleiro() {
		return codJornaleiro;
	}
	public void setCodJornaleiro(Integer codJornaleiro) {
		this.codJornaleiro = codJornaleiro;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public BigDecimal getValorDoBoleto() {
		return valorDoBoleto;
	}
	public void setValorDoBoleto(BigDecimal valorDoBoleto) {
		this.valorDoBoleto = valorDoBoleto;
	}
	public BigDecimal getValorPago() {
		return valorPago;
	}
	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}
	public String getNossoNumeroConsolidado() {
		return nossoNumeroConsolidado;
	}
	public void setNossoNumeroConsolidado(String nossoNumeroConsolidado) {
		this.nossoNumeroConsolidado = nossoNumeroConsolidado;
	}
	public Integer getQtdRegistros() {
		return qtdRegistros;
	}
	public void setQtdRegistros(Integer qtdRegistros) {
		this.qtdRegistros = qtdRegistros;
	}
	
}
