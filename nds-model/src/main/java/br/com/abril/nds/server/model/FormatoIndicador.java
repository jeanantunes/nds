package br.com.abril.nds.server.model;


public enum FormatoIndicador {

	DECIMAL(""),
	MONETARIO("0.00"),
	DATA("dd/MM/yyyy"),
	TEXTO("");
	
	private String formato;
	
	private FormatoIndicador(String formato){
		
		this.formato = formato;
	}
	
	public String getFormato(){
		
		return this.formato;
	}
}