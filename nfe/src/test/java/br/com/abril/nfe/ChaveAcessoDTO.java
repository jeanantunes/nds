package br.com.abril.nfe;

public class ChaveAcessoDTO {
	
	private String anoMes;

	private String numeroNota;
	
	private String serie;
	
	public ChaveAcessoDTO(String anoMes, String numeroNota, String serie) {
		super();
		this.anoMes = anoMes;
		this.numeroNota = numeroNota;
		this.serie = serie;
	}

	public String getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(String numeroNota) {
		this.numeroNota = numeroNota;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getAnoMes() {
		return anoMes;
	}

	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}
}