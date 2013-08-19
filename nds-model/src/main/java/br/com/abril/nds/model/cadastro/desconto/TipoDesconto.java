package br.com.abril.nds.model.cadastro.desconto;

public enum TipoDesconto {
	
    GERAL("Geral"),
	ESPECIFICO("Específico"),
	PRODUTO("Produto");
	
	private String descricao;
	
	private TipoDesconto(String descricao) {
        this.descricao = descricao;
    }
	
	public String getDescricao() {
        return descricao;
    }
	
}
