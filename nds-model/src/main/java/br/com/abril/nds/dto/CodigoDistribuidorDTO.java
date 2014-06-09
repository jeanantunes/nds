package br.com.abril.nds.dto;

public class CodigoDistribuidorDTO {

	private String dinap;
	
	private String fc;

	public CodigoDistribuidorDTO(String dinap, String fc) {
		this.dinap=dinap;
		this.fc=fc;
	}
	
	public String getDinap() {
		return dinap;
	}

	public void setDinap(String dinap) {
		this.dinap = dinap;
	}

	public String getFc() {
		return fc;
	}

	public void setFc(String fc) {
		this.fc = fc;
	}
}
