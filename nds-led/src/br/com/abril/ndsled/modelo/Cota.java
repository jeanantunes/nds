package br.com.abril.ndsled.modelo;

public class Cota {
	private Integer codigoCota;
	private Integer codLed;

	public Integer getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
	}

	public Integer getCodLed() {
		return codLed;
	}

	public void setCodLed(Integer codLed) {
		this.codLed = codLed;
	}

	@Override
	public String toString() {
		return "Cota: " + codigoCota;
	}

}
