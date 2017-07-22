package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class NegociacaoDividaDTO implements Serializable {
	
	private static final long serialVersionUID = 8273101897954671949L;
	
	@Export(label = "Número Cota", widthPercent = 9)
	private Integer numeroCota;
	
	@Export(label = "Data Emissão", widthPercent = 9)
	private Date dtEmissao;
	
	@Export(label = "Data Vencimento", widthPercent = 10)
	private Date dtVencimento;
	
	@Export(label = "Prazo", widthPercent = 9)
	private Integer prazo;

	@Export(label = "Valor Dívida R$", widthPercent = 9)
	private BigDecimal vlDivida;

	@Export(label = "Encargos", widthPercent = 8)
	private BigDecimal encargos;

	@Export(label = "Total R$", widthPercent = 9)
	private BigDecimal total;
	
	@Export(label = "Detalhes", widthPercent = 36)
	private String detalhes;
	
	private Long idDivida;

	private Long idCobranca;
	
	public Date getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(Date dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public Date getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public BigDecimal getVlDivida() {
		return vlDivida;
	}

	public void setVlDivida(BigDecimal vlDivida) {
		this.vlDivida = vlDivida;
	}

	public BigDecimal getEncargos() {
		return encargos;
	}

	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		
		if(total != null){
			this.total = CurrencyUtil.arredondarValorParaDuasCasas(total);
		}else{
			this.total = total;
		}
		
	}

	public Long getIdCobranca() {
		return idCobranca;
	}

	public void setIdCobranca(Long idCobranca) {
		this.idCobranca = idCobranca;
	}

	public Long getIdDivida() {
		return idDivida;
	}

	public void setIdDivida(Long idDivida) {
		this.idDivida = idDivida;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String data, String valor, String observacao) {

		String detalhes = "";
		
		if(this.detalhes != null && !this.detalhes.isEmpty()){
			detalhes = this.detalhes+"\n";
		}
		
		if(!data.isEmpty()){
			detalhes += "Data: "+data+". ";
		}
		
		if(!valor.isEmpty()){
			detalhes += "Valor: R$ "+valor+". \n";
		}
		
		if(!valor.isEmpty()){
			detalhes += "Observação: "+observacao+".";
		}
		
		this.detalhes = detalhes;
	}

}
