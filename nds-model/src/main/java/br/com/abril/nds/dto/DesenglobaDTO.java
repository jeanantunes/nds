package br.com.abril.nds.dto;

import java.io.Serializable;

public class DesenglobaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer desenglobaNumeroCota;
	private String desenglobaNomePessoa;
	private Integer englobadaNumeroCota;
	private String englobadaNomePessoa;
	private Float englobadaPorcentagemCota;
	
	public Integer getDesenglobaNumeroCota() {
		return desenglobaNumeroCota;
	}
	public void setDesenglobaNumeroCota(Integer desenglobaNumeroCota) {
		this.desenglobaNumeroCota = desenglobaNumeroCota;
	}
	public String getDesenglobaNomePessoa() {
		return desenglobaNomePessoa;
	}
	public void setDesenglobaNomePessoa(String desenglobaNomePessoa) {
		this.desenglobaNomePessoa = desenglobaNomePessoa;
	}
	public Integer getEnglobadaNumeroCota() {
		return englobadaNumeroCota;
	}
	public void setEnglobadaNumeroCota(Integer englobadaNumeroCota) {
		this.englobadaNumeroCota = englobadaNumeroCota;
	}
	public String getEnglobadaNomePessoa() {
		return englobadaNomePessoa;
	}
	public void setEnglobadaNomePessoa(String englobadaNomePessoa) {
		this.englobadaNomePessoa = englobadaNomePessoa;
	}
	public Float getEnglobadaPorcentagemCota() {
		return englobadaPorcentagemCota;
	}
	public void setEnglobadaPorcentagemCota(Float englobadaPorcentagemCota) {
		this.englobadaPorcentagemCota = englobadaPorcentagemCota;
	}

}
