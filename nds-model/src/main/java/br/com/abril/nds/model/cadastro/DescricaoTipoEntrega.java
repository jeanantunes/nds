package br.com.abril.nds.model.cadastro;

public enum DescricaoTipoEntrega {

	COTA_RETIRA("Cota Retira"),
	ENTREGA_EM_BANCA("Entrega em Banca"),
	ENTREGADOR("Entregador");
	
	private String value;
	
	private DescricaoTipoEntrega(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
