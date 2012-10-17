package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class ContasApagarConsultaPorDistribuidorVO {

	private String data;
	private String consignado;
	private String suplementacao;
	private String encalhe;
	private String venda;
	private String faltasSobras;
	private String debitoCredito;
	private String saldo;
	
	
	public ContasApagarConsultaPorDistribuidorVO()
	{}
	
	
	public ContasApagarConsultaPorDistribuidorVO(ContasApagarConsultaPorDistribuidorDTO dto) {
		
		this.data = DateUtil.formatarDataPTBR(dto.getData());
		this.consignado = CurrencyUtil.formatarValor(dto.getConsignado());
		this.suplementacao = CurrencyUtil.formatarValor(dto.getSuplementacao());
		this.encalhe = CurrencyUtil.formatarValor(dto.getEncalhe());
		this.venda = CurrencyUtil.formatarValor(dto.getVenda());
		this.faltasSobras = CurrencyUtil.formatarValor(dto.getFaltasSobras());
		this.debitoCredito = CurrencyUtil.formatarValor(dto.getDebitoCredito());
		this.saldo = CurrencyUtil.formatarValor(dto.getSaldo());
	}
	
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getConsignado() {
		return consignado;
	}
	public void setConsignado(String consignado) {
		this.consignado = consignado;
	}
	public String getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(String suplementacao) {
		this.suplementacao = suplementacao;
	}
	public String getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(String encalhe) {
		this.encalhe = encalhe;
	}
	public String getVenda() {
		return venda;
	}
	public void setVenda(String venda) {
		this.venda = venda;
	}
	public String getFaltasSobras() {
		return faltasSobras;
	}
	public void setFaltasSobras(String faltasSobras) {
		this.faltasSobras = faltasSobras;
	}
	public String getDebitoCredito() {
		return debitoCredito;
	}
	public void setDebitoCredito(String debitoCredito) {
		this.debitoCredito = debitoCredito;
	}
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
}
