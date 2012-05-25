package br.com.abril.nds.model;

public enum LeiautePicking {

	UM("PICKING1.TXT"), 
	DOIS("PICKING2.TXT"),
	TRES("PICKING3.TXT");
	
	private String nomeArquivo;
	
	private LeiautePicking(String nomeArquivo) {
		
		this.nomeArquivo = nomeArquivo;
	}
	
	public String getNomeArquivo() {
		
		return nomeArquivo;
	}
	
}
