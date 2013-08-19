package br.com.abril.nds.model.cadastro;

/**
 * 
 * 
 * @author Diego Fernandes
 * 
 */
public enum TipoGarantia {
	
    FIADOR("Fiador"), 
    CHEQUE_CAUCAO("Cheque Caução"), 
    IMOVEL("Imóvel"), 
    NOTA_PROMISSORIA("Nota Promissória"), 
    CAUCAO_LIQUIDA("Caução Liquida"), 
    ANTECEDENCIA_VALIDADE("Antecedência Validade"), 
    OUTROS("Outros");
    
	private String descricao;
	
	private TipoGarantia(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescTipoGarantia(){
		return this.descricao;
	}
	
	public String getDescricao(){
		return this.descricao;
	}
	
	public static TipoGarantia obterPorDescricao(String descricao){
		for (TipoGarantia item : TipoGarantia.values()){
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
