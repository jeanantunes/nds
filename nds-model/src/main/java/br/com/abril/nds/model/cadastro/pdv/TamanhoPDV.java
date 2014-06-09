package br.com.abril.nds.model.cadastro.pdv;

/**
 * Enumeração com o domínio de tamanhos 
 * de PDV disponíveis
 * 
 * @author francisco.garcia
 *
 */
public enum TamanhoPDV {
	
	P("2m até 4m"), 
	M("4m até 8m"), 
	G("8m até 12m"), 
	SG("acima 12m");
	
	private String descricao;

	private TamanhoPDV(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
