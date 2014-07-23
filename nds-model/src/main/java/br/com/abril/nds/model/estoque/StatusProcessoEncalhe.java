package br.com.abril.nds.model.estoque;

public enum StatusProcessoEncalhe {
	
	INICIADO("Iniciado"),
	FINALIZADO("Finalizado"),
	INTERROMPIDO("Interrompido");
	
	private String descricao;
	
	private StatusProcessoEncalhe(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		
		return this.descricao;
	}

}
