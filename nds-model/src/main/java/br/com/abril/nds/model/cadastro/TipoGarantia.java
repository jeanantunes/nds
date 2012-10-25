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
    CAUCAO_LIQUIDA("Caução Líquida"), 
    ANTECEDENCIA_VALIDADE("Antecedência Validade"), 
    OUTROS("Outros");
    
    private String descricao;

    /**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	TipoGarantia(String descricao){
    
		this.descricao = descricao;
    }
    
}
