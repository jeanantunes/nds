package br.com.abril.nds.dto;

public class CotasDesenglobadasDTO extends UsuarioLogDTO {

	private static final long serialVersionUID = -5837269551516923036L;

	private Integer numeroCota;
	private String nomePessoa;
	private String tipoPdv;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getTipoPdv() {
		return tipoPdv;
	}

	public void setTipoPdv(String tipoPdv) {
		this.tipoPdv = tipoPdv;
	}

}
