package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FechamentoCEIntegracaoVO implements Serializable {

	private static final long serialVersionUID = 2253692708185468228L;

	private TableModel<CellModelKeyValue<ItemFechamentoCEIntegracaoDTO>> listaFechamento;

	private boolean semanaFechada;
	
	@Export(label = "Total Bruto R$", alignment=Alignment.RIGHT, alignWithHeader = "Sequencial")
	private String totalBruto;
	
	@Export(label = "Total Desconto R$", alignment=Alignment.RIGHT, alignWithHeader = "Código")
	private String totalDesconto;
	
	@Export(label = "Total Líquido R$", alignment=Alignment.RIGHT, alignWithHeader = "Produto")
	private String totalLiquido;

	public TableModel<CellModelKeyValue<ItemFechamentoCEIntegracaoDTO>> getListaFechamento() {
		return listaFechamento;
	}

	public void setListaFechamento(
			TableModel<CellModelKeyValue<ItemFechamentoCEIntegracaoDTO>> listaFechamento) {
		this.listaFechamento = listaFechamento;
	}

	public boolean isSemanaFechada() {
		return semanaFechada;
	}

	public void setSemanaFechada(boolean semanaFechada) {
		this.semanaFechada = semanaFechada;
	}

	public String getTotalBruto() {
		return totalBruto;
	}

	public void setTotalBruto(String totalBruto) {
		this.totalBruto = totalBruto;
	}

	public String getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(String totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public String getTotalLiquido() {
		return totalLiquido;
	}

	public void setTotalLiquido(String totalLiquido) {
		this.totalLiquido = totalLiquido;
	}

}
