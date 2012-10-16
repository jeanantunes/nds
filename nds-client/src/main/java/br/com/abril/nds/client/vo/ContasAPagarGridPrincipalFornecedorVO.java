package br.com.abril.nds.client.vo;

import java.util.ArrayList;

import br.com.abril.nds.dto.ContasAPagarGridPrincipalFornecedorDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.util.CurrencyUtil;


public class ContasAPagarGridPrincipalFornecedorVO extends FlexiGridVO<ContasApagarConsultaPorDistribuidorVO> {

	private String totalBruto;
	private String totalDesconto;
	private String saldo;
	
	
	public ContasAPagarGridPrincipalFornecedorVO()
	{}
	
	
	public ContasAPagarGridPrincipalFornecedorVO(ContasAPagarGridPrincipalFornecedorDTO dto) {
		
		this.totalBruto = CurrencyUtil.formatarValor(dto.getTotalBruto());
		this.totalDesconto = CurrencyUtil.formatarValor(dto.getTotalDesconto());
		this.saldo = CurrencyUtil.formatarValor(dto.getSaldo());
		
		this.grid = new ArrayList<ContasApagarConsultaPorDistribuidorVO>();
		for (ContasApagarConsultaPorDistribuidorDTO obj : dto.getGrid()) {
			this.grid.add(new ContasApagarConsultaPorDistribuidorVO(obj));
		}
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
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
}
