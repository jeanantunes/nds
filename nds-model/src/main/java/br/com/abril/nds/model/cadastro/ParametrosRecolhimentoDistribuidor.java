package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParametrosRecolhimentoDistribuidor implements Serializable {

	private static final long serialVersionUID = 1755370508652128006L;

	@Column(name = "CONFERENCIA_CEGA_ENCALHE",nullable=true)
	private boolean conferenciaCegaEncalhe;	
	
	@Column(name = "CONFERENCIA_CEGA_RECEBIMENTO",nullable=true)
	private boolean conferenciaCegaRecebimento;
	
	@Column(name = "DIA_RECOLHIMENTO_PRIMEIRO",nullable=true)
	private boolean diaRecolhimentoPrimeiro;
	
	@Column(name = "DIA_RECOLHIMENTO_SEGUNDO",nullable=true)
	private boolean diaRecolhimentoSegundo;	
	
	@Column(name = "DIA_RECOLHIMENTO_TERCEIRO",nullable=true)
	private boolean diaRecolhimentoTerceiro;
	
	@Column(name = "DIA_RECOLHIMENTO_QUARTO",nullable=true)
	private boolean diaRecolhimentoQuarto;
	
	@Column(name = "DIA_RECOLHIMENTO_QUINTO",nullable=true)
	private boolean diaRecolhimentoQuinto;
	
	/**
	 * Parâmetro que indica se o distribuidor aceita a
	 * realização de conferência de encalhe antecipada.
	 */
	@Column(name = "PERMITE_RECOLHER_DIAS_POSTERIORES",nullable=true)
	private boolean permiteRecolherDiasPosteriores;

	public boolean isConferenciaCegaEncalhe() {
		return conferenciaCegaEncalhe;
	}

	public void setConferenciaCegaEncalhe(boolean conferenciaCegaEncalhe) {
		this.conferenciaCegaEncalhe = conferenciaCegaEncalhe;
	}

	public boolean isConferenciaCegaRecebimento() {
		return conferenciaCegaRecebimento;
	}

	public void setConferenciaCegaRecebimento(boolean conferenciaCegaRecebimento) {
		this.conferenciaCegaRecebimento = conferenciaCegaRecebimento;
	}

	public boolean isDiaRecolhimentoPrimeiro() {
		return diaRecolhimentoPrimeiro;
	}

	public void setDiaRecolhimentoPrimeiro(boolean diaRecolhimentoPrimeiro) {
		this.diaRecolhimentoPrimeiro = diaRecolhimentoPrimeiro;
	}

	public boolean isDiaRecolhimentoSegundo() {
		return diaRecolhimentoSegundo;
	}

	public void setDiaRecolhimentoSegundo(boolean diaRecolhimentoSegundo) {
		this.diaRecolhimentoSegundo = diaRecolhimentoSegundo;
	}

	public boolean isDiaRecolhimentoTerceiro() {
		return diaRecolhimentoTerceiro;
	}

	public void setDiaRecolhimentoTerceiro(boolean diaRecolhimentoTerceiro) {
		this.diaRecolhimentoTerceiro = diaRecolhimentoTerceiro;
	}

	public boolean isDiaRecolhimentoQuarto() {
		return diaRecolhimentoQuarto;
	}

	public void setDiaRecolhimentoQuarto(boolean diaRecolhimentoQuarto) {
		this.diaRecolhimentoQuarto = diaRecolhimentoQuarto;
	}

	public boolean isDiaRecolhimentoQuinto() {
		return diaRecolhimentoQuinto;
	}

	public void setDiaRecolhimentoQuinto(boolean diaRecolhimentoQuinto) {
		this.diaRecolhimentoQuinto = diaRecolhimentoQuinto;
	}

	public boolean isPermiteRecolherDiasPosteriores() {
		return permiteRecolherDiasPosteriores;
	}

	public void setPermiteRecolherDiasPosteriores(
			boolean permiteRecolherDiasPosteriores) {
		this.permiteRecolherDiasPosteriores = permiteRecolherDiasPosteriores;
	}
	
}
