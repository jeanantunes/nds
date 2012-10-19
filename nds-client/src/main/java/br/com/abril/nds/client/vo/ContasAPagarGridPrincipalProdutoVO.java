package br.com.abril.nds.client.vo;

import java.util.ArrayList;

import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.util.CurrencyUtil;

public class ContasAPagarGridPrincipalProdutoVO extends FlexiGridVO<ContasAPagarConsultaPorProdutoVO>{

	private String totalPagto;
	private String totalDesconto;
	private String valorLiquido;
	
	
	public ContasAPagarGridPrincipalProdutoVO()
	{
		super();
	}
	
	
	public ContasAPagarGridPrincipalProdutoVO(ContasAPagarGridPrincipalProdutoDTO dto) {
		
		this.totalPagto = CurrencyUtil.formatarValor(dto.getTotalPagto());
		this.totalDesconto = CurrencyUtil.formatarValor(dto.getTotalDesconto());
		this.valorLiquido = CurrencyUtil.formatarValor(dto.getValorLiquido());
		this.totalGrid = dto.getTotalGrid();
		
		grid = new ArrayList<ContasAPagarConsultaPorProdutoVO>();
		for (ContasApagarConsultaPorProdutoDTO obj : dto.getGrid()) {
			grid.add(new ContasAPagarConsultaPorProdutoVO(obj));
		}
	}
	
	
	public String getTotalPagto() {
		return totalPagto;
	}
	public void setTotalPagto(String totalPagto) {
		this.totalPagto = totalPagto;
	}
	public String getTotalDesconto() {
		return totalDesconto;
	}
	public void setTotalDesconto(String totalDesconto) {
		this.totalDesconto = totalDesconto;
	}
	public String getValorLiquido() {
		return valorLiquido;
	}
	public void setValorLiquido(String valorLiquido) {
		this.valorLiquido = valorLiquido;
	}
}
