package br.com.abril.nds.model.cadastro;

public enum ModalidadeCobranca{
	
	TAXA_FIXA("TF", "Taxa Fixa"),
	PERCENTUAL("P", "Percentual");
	
	private String codigo;
	
	private String descricao;
	
	private ModalidadeCobranca(String codigo, String descricao){
		
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}