package br.com.abril.nds.model.cadastro;

public enum TipoArquivo {

	TXT("txt"),
	PDF("pdf");
	
	private String descricao;
	
	private TipoArquivo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoArquivo(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}	
	
}
