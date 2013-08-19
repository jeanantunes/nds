package br.com.abril.nds.model.cadastro;

public enum ClassificacaoEspectativaFaturamento {
	
	AA ("AA -R$ 4.000,00 a R$ 999999,99 (mais de 1 PDV)"),
	A  ("A - R$ 4.000,00 a R$ 999999,99"),
	B  ("B - R$ 2.000,00 a R$ 3.999,99"),
	C  ("C - R$ 1.000,00 a R$ 1.999,99"),
	D  ("D - R$ 0.01 a R$ 999,99");
	
	private String descricao;
	
	private ClassificacaoEspectativaFaturamento(String descricao) {
		
		this.descricao = descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
}
