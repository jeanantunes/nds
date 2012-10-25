package br.com.abril.nds.model.cadastro;

/**
 * 
 * 
 * @author Jones Nogueira
 * 
 */
public enum TipoStatusGarantia {
    VENCIDA("Vencida"), 
    A_VENCER("A Vencer");
    
	private String descricao;
	
	private TipoStatusGarantia(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoStatusGarantia(){
		return this.descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}
