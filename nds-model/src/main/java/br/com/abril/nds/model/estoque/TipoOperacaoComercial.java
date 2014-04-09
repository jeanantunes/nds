package br.com.abril.nds.model.estoque;


/**
 * @author luiz.marcili
 * @version 1.0
 * @created 15-out-2012 13:47:33
 */
public enum TipoOperacaoComercial {
	
	CONSEGNADO("Consegnado"),
	VENDA("Venda");
	
	private String descricao;
	
	private TipoOperacaoComercial (String descricao){
		this.descricao = descricao;
	}
	
	public static TipoOperacaoComercial obterPelaDescricao(String descricao) {
		
		for (TipoOperacaoComercial tipoOper : TipoOperacaoComercial.values()) {
			
			if (tipoOper.getDescricao().equals(descricao)) {
				
				return tipoOper;
			}
		}
		
		return null;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
}