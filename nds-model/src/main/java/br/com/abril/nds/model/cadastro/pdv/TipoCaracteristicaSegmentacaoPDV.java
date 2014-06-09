package br.com.abril.nds.model.cadastro.pdv;

/**
 * Tipo de característica de segmentação do PDV
 * 
 * @author francisco.garcia
 *
 */
public enum TipoCaracteristicaSegmentacaoPDV {
	
	CONVENCIONAL("Convencional"), 
	
	ALTERNATIVO("Alternativo");
	
	TipoCaracteristicaSegmentacaoPDV(String descricao){
		this.descricao = descricao;
	}
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}

}
