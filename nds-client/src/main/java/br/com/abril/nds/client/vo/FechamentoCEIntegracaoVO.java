package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class FechamentoCEIntegracaoVO implements Serializable {

	private static final long serialVersionUID = 2253692708185468228L;

	private TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> listaFechamento;

	private boolean semanaFechada;

	private String totalBruto;

	private String totalDesconto;

	private String totalLiquido;

	public TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> getListaFechamento() {
		return listaFechamento;
	}

	public void setListaFechamento(
			TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> listaFechamento) {
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
