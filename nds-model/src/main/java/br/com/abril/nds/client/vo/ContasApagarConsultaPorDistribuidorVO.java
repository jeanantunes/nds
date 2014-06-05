package br.com.abril.nds.client.vo;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ContasApagarConsultaPorDistribuidorVO {

	@Export(label = "Data", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String data;
	
	@Export(label = "Consignado R$", alignment=Alignment.RIGHT, exhibitionOrder = 2)
	private String consignado;
	
	@Export(label = "Suplementação R$", alignment=Alignment.RIGHT, exhibitionOrder = 3)
	private String suplementacao;
	
	@Export(label = "Encalhe R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String encalhe;
	
	@Export(label = "Venda R$", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private String venda;
	
	@Export(label = "Faltas Sobras R$", alignment=Alignment.RIGHT, exhibitionOrder = 6)
	private String faltasSobras;
	
	@Export(label = "Deb/Cred R$", alignment=Alignment.RIGHT, exhibitionOrder = 7)
	private String debitoCredito;
	
	@Export(label = "Saldo a Pagar R$", alignment=Alignment.RIGHT, exhibitionOrder = 8)
	private String saldo;
	
	
	public ContasApagarConsultaPorDistribuidorVO()
	{}
	
	
	public ContasApagarConsultaPorDistribuidorVO(Date dataMovimento, BigDecimal consignado, BigDecimal encalhe, 
            BigDecimal suplementacao, BigDecimal faltasSobras, BigDecimal perdasGanhos){
        
        this.data = DateUtil.formatarDataPTBR(dataMovimento);
        this.consignado = CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(consignado));
        this.encalhe = CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(encalhe));
        
        if (consignado == null){
            
            consignado = BigDecimal.ZERO;
        }
        
        if (encalhe == null){
            
            encalhe = BigDecimal.ZERO;
        }
        
        this.venda = CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(consignado.subtract(encalhe)));
        
        this.suplementacao = CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(suplementacao));
        
        this.faltasSobras = CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(faltasSobras));
        
        this.debitoCredito = CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(perdasGanhos));
        
        this.saldo = CurrencyUtil.formatarValor(
                CurrencyUtil.arredondarValorParaDuasCasas(
                        consignado.subtract(encalhe == null ? BigDecimal.ZERO : encalhe)
                        .subtract(faltasSobras == null ? BigDecimal.ZERO : faltasSobras)
                        .subtract(perdasGanhos == null ? BigDecimal.ZERO : perdasGanhos)));
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
