package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RelatorioDetalheGarantiaVO implements Serializable{

	private static final long serialVersionUID = 7317808904298274920L;

	@Export(label="Cota", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private String cota;
	
	@Export(label="Nome", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String nome;
	
	@Export(label="Garantia", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private String garantia;
	
	@Export(label="Vencimento", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private String vencto;
	
	@Export(label="Valor Garantia R$", alignment = Alignment.RIGHT, exhibitionOrder = 5)
	private String vlrGarantia;
	
	@Export(label="Faturamento ", alignment = Alignment.RIGHT, exhibitionOrder = 6)
	private String faturamento;
	
	@Export(label="% Garantia s/ Fat.", alignment = Alignment.RIGHT, exhibitionOrder = 7)
	private String garantiaFaturamento;
	
	
	public RelatorioDetalheGarantiaVO() {
		super();
	}

	public RelatorioDetalheGarantiaVO(RelatorioDetalheGarantiaDTO dto) {
		this.cota = dto.getCota().toString();
		this.nome = dto.getNome();
		this.garantia = dto.getGarantia();
		this.garantiaFaturamento = CurrencyUtil.formatarValor(dto.getFaturamento());
		this.vencto = DateUtil.formatarDataPTBR(dto.getVencto());
		this.faturamento = CurrencyUtil.formatarValor(dto.getFaturamento());
		this.garantiaFaturamento = CurrencyUtil.formatarValor(dto.getGarantiaFaturamento());
		this.vlrGarantia = CurrencyUtil.formatarValor(dto.getVlrGarantia());
	}
	
	public String getCota() {
		return cota;
	}
	public void setCota(String cota) {
		this.cota = cota;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getGarantia() {
		return garantia;
	}
	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}
	public String getVencto() {
		return vencto;
	}
	public void setVencto(String vencto) {
		this.vencto = vencto;
	}
	public String getVlrGarantia() {
		return vlrGarantia;
	}
	public void setVlrGarantia(String vlrGarantia) {
		this.vlrGarantia = vlrGarantia;
	}
	public String getFaturamento() {
		return faturamento;
	}
	public void setFaturamento(String faturamento) {
		this.faturamento = faturamento;
	}
	public String getGarantiaFaturamento() {
		return garantiaFaturamento;
	}
	public void setGarantiaFaturamento(String garantiaFaturamento) {
		this.garantiaFaturamento = garantiaFaturamento;
	}
	
	
	
	
}
