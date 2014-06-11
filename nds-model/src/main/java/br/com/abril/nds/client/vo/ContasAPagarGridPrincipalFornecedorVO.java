package br.com.abril.nds.client.vo;


public class ContasAPagarGridPrincipalFornecedorVO extends FlexiGridVO<ContasApagarConsultaPorDistribuidorVO> {

	private String totalBruto;
	private String totalDesconto;
	private String saldo;
	
	
	public ContasAPagarGridPrincipalFornecedorVO(){}
	
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
