package br.com.abril.nds.model.cadastro;

public enum Processo {
	
	ENVIO("Envio de repartes"),
	VENDA("Venda"),
	ENTRADA_ENCALHE("Entrada do encalhe"),
	FALTA_REPARTE("Faltas do reparte"),
	SOBRA_REPARTE("Sobras do reparte"),
	FALTA_ENCALHE("Faltas do encalhe"),
	SOBRA_ENCALHE("Sobras do encalhe"),
	DEVOLUCAO_FORNECEDOR("Devolução fornecedor"),

	GERACAO_NF_E("Geração NF-e"),
	LANCAMENTO_FALTA_SOBRA("Lançamento faltas e sobra"),
	VENDA_SUPLEMENTAR("Venda de Suplementar"),
	FECHAMENTO_ENCALHE("Fechamento de Encalhe"),
	DEVOLUCAO_AO_FORNECEDOR("Devolução ao Fornecedor"), 
	DEVOLUCAO_ENCALHE("Nota da Devolução do Encalhe"),
	CONSIGNACAO_REPARTE_NORMAL("Consignação do Reparte Normal"),
	NOTA_LANCAMENTO("Nota de Lançamento (NE/NECA)")
	;
	private String processo;
	
	Processo(String processo) {
		this.processo = processo;
	}
	
	public String getDescricao(){
		return this.processo;
	}
	
}
