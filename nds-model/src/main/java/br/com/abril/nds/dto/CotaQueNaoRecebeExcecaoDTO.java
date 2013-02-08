package br.com.abril.nds.dto;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;

public class CotaQueNaoRecebeExcecaoDTO {

	private Integer numeroCota;
	private String nomePessoa;
	private SituacaoCadastro statusCota;

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

	public SituacaoCadastro getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(SituacaoCadastro statusCota) {
		this.statusCota = statusCota;
	}

}
