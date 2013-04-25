package br.com.abril.nds.model.distribuicao;

public enum TipoPesquisaCaracteristicaDistribuicao {
	INICIA(1), CONTEM(2),EXATO(3);
	
	private int value;

	private TipoPesquisaCaracteristicaDistribuicao(int value) {
		this.value = value;
		
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
