package br.com.abril.nds.model.cadastro;

public enum Processo {
	
	LANCAMENTO_FALTA_SOBRA("Lançamento faltas e sobra"),
	GERACAO_NF_E("Geração NF-e"),
	VENDA_SUPLEMENTAR("Venda de Suplementar"),
	FECHAMENTO_ENCALHE("Fechamento de Encalhe"),
	DEVOLUCAO_AO_FORNECEDOR("Devolução ao Fornecedor")
	;
	private String processo;
	
	Processo(String processo) {
		this.processo = processo;
	}
	
	public String getDescricao(){
		return this.processo;
	}
	
}
