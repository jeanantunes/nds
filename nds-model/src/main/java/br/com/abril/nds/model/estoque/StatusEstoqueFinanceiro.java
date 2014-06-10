package br.com.abril.nds.model.estoque;


/**
 * @author luiz.marcili
 * @version 1.0
 * @created 15-out-2012 13:47:33
 */
public enum StatusEstoqueFinanceiro {
	
	FINANCEIRO_PROCESSADO("FINANCEIRO PROCESSADO"),
	
	FINANCEIRO_NAO_PROCESSADO("FINANCEIRO NÂO PROCESSADO");
	
	private String descricao;
	
	private StatusEstoqueFinanceiro (String descricao){
		this.descricao = descricao;
	}
	
	public static StatusEstoqueFinanceiro obterPelaDescricao(String descricao) {
		
		for (StatusEstoqueFinanceiro status : StatusEstoqueFinanceiro.values()) {
			
			if (status.getDescricao().equals(descricao)) {
				
				return status;
			}
		}
		
		return null;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
}