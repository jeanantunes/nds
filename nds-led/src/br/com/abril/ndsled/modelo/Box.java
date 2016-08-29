package br.com.abril.ndsled.modelo;

public class Box {

	private Integer codigoBox;
	private Integer quantidade;

	public Integer getCodigoBox() {
		return codigoBox;
	}

	public void setCodigoBox(Integer codigoBox) {
		this.codigoBox = codigoBox;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public String toString() {
		return "Box " + codigoBox + " - " + quantidade + "Exs." + "]";
	}
	
	
}
