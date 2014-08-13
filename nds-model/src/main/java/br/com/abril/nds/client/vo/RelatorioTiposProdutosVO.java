package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RelatorioTiposProdutosVO implements Serializable {

	private static final long serialVersionUID = 1406337437334883137L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2, widthPercent=35)
	private String produto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String edicao;
	
	@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String precoCapa;
	
	@Export(label = "Faturamento R$", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private String faturamento;
	
	@Export(label = "Tipo Produto", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String tipoProduto;
	
	@Export(label = "Lcto", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String lancamento;
	
	@Export(label = "Rclto", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private String recolhimento;
	
	
	
	public RelatorioTiposProdutosVO()
	{}
	
	
	public RelatorioTiposProdutosVO(RelatorioTiposProdutosDTO dto) {
		
		this.setCodigo(dto.getCodigo().toString());
		this.setProduto(dto.getProduto());
		this.setEdicao(dto.getEdicao().toString());
		this.setPrecoCapa(CurrencyUtil.formatarValor(dto.getPrecoCapa()));
		this.setFaturamento(CurrencyUtil.formatarValor(dto.getFaturamento()));
		this.setTipoProduto(dto.getTipoProduto());
		this.setLancamento(DateUtil.formatarDataPTBR(dto.getLancamento()));
		this.setRecolhimento(DateUtil.formatarDataPTBR(dto.getRecolhimento()));
	}
	
	
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public String getEdicao() {
		return edicao;
	}
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	public String getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}
	public String getFaturamento() {
		return faturamento;
	}
	public void setFaturamento(String faturamento) {
		this.faturamento = faturamento;
	}
	public String getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	public String getLancamento() {
		return lancamento;
	}
	public void setLancamento(String lancamento) {
		this.lancamento = lancamento;
	}
	public String getRecolhimento() {
		return recolhimento;
	}
	public void setRecolhimento(String recolhimento) {
		this.recolhimento = recolhimento;
	}
}
