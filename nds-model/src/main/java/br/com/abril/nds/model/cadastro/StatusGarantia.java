package br.com.abril.nds.model.cadastro;

/**
 * 
 * 
 * @author Henrique
 * 
 */
public enum StatusGarantia {
	
    VENCIDA("Vencida"), 
    A_VENCER("A Vencer");
    
	private String descricao;
	
	private StatusGarantia(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoGarantia(){
		return this.descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	public static StatusGarantia obterPorDescricao(String descricao){
		for (StatusGarantia item : StatusGarantia.values()){
			if (item.getDescricao().equals(descricao)){
				return item;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}
}
