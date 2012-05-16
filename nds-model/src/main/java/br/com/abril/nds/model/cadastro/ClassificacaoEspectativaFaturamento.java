package br.com.abril.nds.model.cadastro;

public enum ClassificacaoEspectativaFaturamento {
	
	AA ("AA -Faturamento - Mais de 1 PDV"),
	A  ("A - Faturamento Acima de R$ 3.500,00"),
	B  ("B - Faturamento entre R$ 1.500,00 - R$ 3.499"),
	C  ("C - Faturamento Abaixo R$ 1.499,00");
	
	private String descricao;
	
	private ClassificacaoEspectativaFaturamento(String descricao) {
		
		this.descricao = descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
}
