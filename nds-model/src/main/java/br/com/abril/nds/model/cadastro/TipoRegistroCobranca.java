package br.com.abril.nds.model.cadastro;

public enum TipoRegistroCobranca {
	
	REGISTRADA("Com registro"),
	SEM_REGISTRO("Sem registro");
	
    private String descricao;
	
	private TipoRegistroCobranca(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}

}
