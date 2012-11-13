package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoEndereco {
	
	COBRANCA("Cobranca"),
	COMERCIAL("Comercial"),
	LOCAL_ENTREGA("Local de Entrega"),
	RESIDENCIAL("Residencial");
	
	private String tipoEndereco;
	
	private TipoEndereco(String tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	/**
	 * @return the tipoEndereco
	 */
	public String getTipoEndereco() {
		return tipoEndereco;
	}

	/**
	 * @param tipoEndereco the tipoEndereco to set
	 */
	public void setTipoEndereco(String tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}
}