package br.com.abril.nds.client.vo;

public class RegistroEdicoesFechadasVO {

	private String codigoProduto;
	private String nomeProduto;
	private String edicaoProduto;
	private String nomeFornecedor;
	private String dataLancamento;
	private String saldo;
	private String recolhimento;
	private String parcial;

	public RegistroEdicoesFechadasVO(Object codigoProduto, Object nomeProduto,
			Object edicaoProduto, Object nomeFornecedor, Object dataLancamento,
			Object saldo, Object recolhimento, Object parcial) {
		System.out.println("teste");
	}
	
}
