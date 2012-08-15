package br.com.abril.nds.client.vo;

public class EntregadorCotaProcuracaoVO {

	
	public EntregadorCotaProcuracaoVO(Integer numeroCota, String nomeCota, boolean procuracaoAssinada){
		
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.procuracaoAssinada = procuracaoAssinada;
	}
	
	private Integer numeroCota;
	
	private String nomeCota;
	
	private boolean procuracaoAssinada;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public boolean isProcuracaoAssinada() {
		return procuracaoAssinada;
	}

	public void setProcuracaoAssinada(boolean procuracaoAssinada) {
		this.procuracaoAssinada = procuracaoAssinada;
	}
}