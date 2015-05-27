package br.com.abril.nds.model.fiscal.nota;


/**
 * Modalidade de determinação da BC do ICMS
 * 
 * @author Diego Fernandes
 * 
 */
public enum OrigemProduto {
	
	NACIONAL(0, "Nacional"), 
	ESTRANGEIRA_IMPORTACAO(1, "Estrangeira"),
	ESTRANGEIRA_MERCADO(2, "Estrangeira – Adquirida no mercado interno");
	
	private Integer id;
	private String descricao;
	
	OrigemProduto(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	
	public int getId() {
		return id.intValue();
	}

	public String getDescricao() {
		return descricao;
	}
}