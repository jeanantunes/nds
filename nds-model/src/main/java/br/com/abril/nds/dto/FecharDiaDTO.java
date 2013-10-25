package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 4116643880953265518L;
	
	private Boolean baixaBancaria = false;
	
	private Boolean geracaoDeCobranca = false;
	
	private Boolean recebimentoFisico = false;
	
	private Boolean confirmacaoDeExpedicao = false;	
	
	private Boolean lancamentoFaltasESobras = false;
	
	private Boolean controleDeAprovacao = false;
	
	private Boolean fechamentoEncalhe = false;
	
	private Boolean habilitarConfirmar = false;
	
	public boolean isFechamentoPermitido() {
		
		return this.baixaBancaria 
				&& this.recebimentoFisico 
				&& this.confirmacaoDeExpedicao 
				&& this.lancamentoFaltasESobras
				&& this.controleDeAprovacao
				&& this.fechamentoEncalhe;
	}

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

	public Boolean getFechamentoEncalhe() {
		return fechamentoEncalhe;
	}

	public void setFechamentoEncalhe(Boolean fechamentoEncalhe) {
		this.fechamentoEncalhe = fechamentoEncalhe;
	}

	public Boolean getHabilitarConfirmar() {
		return habilitarConfirmar;
	}

	public void setHabilitarConfirmar(Boolean habilitarConfirmar) {
		this.habilitarConfirmar = habilitarConfirmar;
	}
	
}
