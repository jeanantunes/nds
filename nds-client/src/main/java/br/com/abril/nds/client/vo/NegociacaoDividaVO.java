package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class NegociacaoDividaVO implements Serializable {
	
	private static final long serialVersionUID = 8273101897954671949L;
	
	public NegociacaoDividaVO (){
		
	}
	
	public NegociacaoDividaVO (Cobranca c){
		this.setDtEmissao(DateUtil.formatarDataPTBR(c.getDataEmissao()));
		this.setDtVencimento(DateUtil.formatarDataPTBR(c.getDataVencimento()));
		this.setPrazo(String.valueOf(DateUtil.obterDiferencaDias(new Date(), c.getDataVencimento())));
		this.setVlDivida(CurrencyUtil.formatarValor(c.getDivida().getValor()));
		this.setEncargos(CurrencyUtil.formatarValor(c.getEncargos()));
		this.setTotal(CurrencyUtil.formatarValor((c.getDivida().getValor().add(c.getEncargos()))));
	}
	@Export(label = "Data Emissão", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private String dtEmissao;
	
	@Export(label = "Data Vencimento", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String dtVencimento;
	
	@Export(label = "Prazo", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String prazo;
	
	@Export(label = "Valor Dívida R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String vlDivida;
	
	@Export(label = "Encargos", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private String encargos;
	
	@Export(label = "Total R$", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String total;
	
	private String detalhes = "";
	
	private String acao = "";

	public String getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(String dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public String getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(String dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public String getPrazo() {
		return prazo;
	}

	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	public String getVlDivida() {
		return vlDivida;
	}

	public void setVlDivida(String vlDivida) {
		this.vlDivida = vlDivida;
	}

	public String getEncargos() {
		return encargos;
	}

	public void setEncargos(String encargos) {
		this.encargos = encargos;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	

}
