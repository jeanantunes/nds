package br.com.abril.nds.model.cadastro.desconto;

public enum TipoDesconto {
	
    GERAL("Geral"),
	ESPECIFICO("Específico"),
	EDITOR("Editor"),
	PRODUTO("Produto");
	
	private String descricao;
	
	private TipoDesconto(String descricao) {
        this.descricao = descricao;
    }
	
	public String getDescricao() {
        return descricao;
    }
	
	public static TipoDesconto getStatusByDescription(String description) {
		for(TipoDesconto status : TipoDesconto.values()) {
			if(status.getDescricao().equalsIgnoreCase(description))
				return status;
		}
		throw new IllegalArgumentException("Description " + description + " is not valid!");
	}
	
}
