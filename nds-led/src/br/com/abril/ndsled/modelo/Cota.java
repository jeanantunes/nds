package br.com.abril.ndsled.modelo;

public class Cota {
	private Integer codigoCota;
	private Integer codLed;
	private Integer reparte;

	public Cota(Integer codigoCota, Integer codLed, Integer reparte) {
		this.codigoCota = codigoCota;
		this.codLed = codLed;
		this.reparte = reparte;
	}

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

	public Integer getReparte() {
		return reparte;
	}

	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}

	@Override
	public String toString() {
		return "Cota: " + codigoCota;
	}

}
