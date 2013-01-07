package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class FechamentoCEIntegracaoVO implements Serializable {

	private static final long serialVersionUID = 2253692708185468228L;

	private TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> listaFechamento;

	private boolean semanaFechada;

	private BigDecimal totalBruto;

	private BigDecimal totalDesconto;

	private BigDecimal totalLiquido;

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

	public BigDecimal getTotalBruto() {
		return totalBruto;
	}

	public void setTotalBruto(BigDecimal totalBruto) {
		this.totalBruto = totalBruto;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public BigDecimal getTotalLiquido() {
		return totalLiquido;
	}

	public void setTotalLiquido(BigDecimal totalLiquido) {
		this.totalLiquido = totalLiquido;
	}

}
