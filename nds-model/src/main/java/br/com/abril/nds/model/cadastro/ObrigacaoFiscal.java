package br.com.abril.nds.model.cadastro;

public enum ObrigacaoFiscal {

	COTA_TOTAL("Cota Total"),
	COTA_NFE_VENDA("Cota NF-e Venda"),
	DEVOLUCAO_FORNECEDOR("Devolução Fornecedor");
	
	private String descricao;
	
	private ObrigacaoFiscal(String descricao) {
		
		this.descricao = descricao;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	
}
