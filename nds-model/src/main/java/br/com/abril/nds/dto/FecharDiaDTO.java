package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 4116643880953265518L;
	
	private Boolean baixaBancaria;
	
	private Boolean geracaoDeCobranca;
	
	private Boolean recebimentoFisico;
	
	private Boolean confirmacaoDeExpedicao;	
	
	private Boolean lancamentoFaltasESobras;
	
	private Boolean controleDeAprovacao;
	
	private Boolean manutencaoStatusCota;

	public Boolean getBaixaBancaria() {
		return baixaBancaria;
	}

	public void setBaixaBancaria(Boolean baixaBancaria) {
		this.baixaBancaria = baixaBancaria;
	}

	public Boolean getGeracaoDeCobranca() {
		return geracaoDeCobranca;
	}

	public void setGeracaoDeCobranca(Boolean geracaoDeCobranca) {
		this.geracaoDeCobranca = geracaoDeCobranca;
	}

	public Boolean getRecebimentoFisico() {
		return recebimentoFisico;
	}

	public void setRecebimentoFisico(Boolean recebimentoFisico) {
		this.recebimentoFisico = recebimentoFisico;
	}

	public Boolean getConfirmacaoDeExpedicao() {
		return confirmacaoDeExpedicao;
	}

	public void setConfirmacaoDeExpedicao(Boolean confirmacaoDeExpedicao) {
		this.confirmacaoDeExpedicao = confirmacaoDeExpedicao;
	}

	public Boolean getLancamentoFaltasESobras() {
		return lancamentoFaltasESobras;
	}

	public void setLancamentoFaltasESobras(Boolean lancamentoFaltasESobras) {
		this.lancamentoFaltasESobras = lancamentoFaltasESobras;
	}

	public Boolean getControleDeAprovacao() {
		return controleDeAprovacao;
	}

	public void setControleDeAprovacao(Boolean controleDeAprovacao) {
		this.controleDeAprovacao = controleDeAprovacao;
	}

	public Boolean getManutencaoStatusCota() {
		return manutencaoStatusCota;
	}

	public void setManutencaoStatusCota(Boolean manutencaoStatusCota) {
		this.manutencaoStatusCota = manutencaoStatusCota;
	}
	
}
