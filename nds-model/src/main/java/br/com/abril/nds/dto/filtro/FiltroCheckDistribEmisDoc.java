package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroCheckDistribEmisDoc implements Serializable{
	
	
	private boolean isSlipImpresso;
	private boolean isSlipEmail;
	
	private boolean isBoletoImpresso;
	private boolean isBoletoEmail;

	private boolean isBoletoSlipImpresso;
	private boolean isBoletoSlipEmail;

	private boolean isReciboImpresso;
	private boolean isReciboEmail;

	private boolean isNoteEnvioImpresso;
	private boolean isNoteEnvioEmail;

	private boolean isChamdaEncalheImpresso;
	private boolean isChamdaEncalheEmail;
	
	public boolean getIsSlipImpresso() {
		return isSlipImpresso;
	}
	public void setIsSlipImpresso(boolean isSkipImpresso) {
		this.isSlipImpresso = isSkipImpresso;
	}
	public boolean getIsSlipEmail() {
		return isSlipEmail;
	}
	public void setIsSlipEmail(boolean isSkipEmail) {
		this.isSlipEmail = isSkipEmail;
	}
	public boolean getIsBoletoImpresso() {
		return isBoletoImpresso;
	}
	public void setIsBoletoImpresso(boolean isBoletoImpresso) {
		this.isBoletoImpresso = isBoletoImpresso;
	}
	public boolean getIsBoletoEmail() {
		return isBoletoEmail;
	}
	public void setIsBoletoEmail(boolean isBoletoEmail) {
		this.isBoletoEmail = isBoletoEmail;
	}
	public boolean getIsBoletoSlipImpresso() {
		return isBoletoSlipImpresso;
	}
	public void setIsBoletoSlipImpresso(boolean isBoletoSkipImpresso) {
		this.isBoletoSlipImpresso = isBoletoSkipImpresso;
	}
	public boolean getIsBoletoSlipEmail() {
		return isBoletoSlipEmail;
	}
	public void setIsBoletoSlipEmail(boolean isBoletoSkipEmail) {
		this.isBoletoSlipEmail = isBoletoSkipEmail;
	}
	public boolean getIsReciboImpresso() {
		return isReciboImpresso;
	}
	public void setIsReciboImpresso(boolean isReciboImpresso) {
		this.isReciboImpresso = isReciboImpresso;
	}
	public boolean getIsReciboEmail() {
		return isReciboEmail;
	}
	public void setIsReciboEmail(boolean isReciboEmail) {
		this.isReciboEmail = isReciboEmail;
	}
	public boolean getIsNoteEnvioImpresso() {
		return isNoteEnvioImpresso;
	}
	public void setIsNoteEnvioImpresso(boolean isNoteEnvioImpresso) {
		this.isNoteEnvioImpresso = isNoteEnvioImpresso;
	}
	public boolean getIsNoteEnvioEmail() {
		return isNoteEnvioEmail;
	}
	public void setIsNoteEnvioEmail(boolean isNoteEnvioEmail) {
		this.isNoteEnvioEmail = isNoteEnvioEmail;
	}
	public boolean getIsChamdaEncalheImpresso() {
		return isChamdaEncalheImpresso;
	}
	public void setIsChamdaEncalheImpresso(boolean isChamdaEncalheImpresso) {
		this.isChamdaEncalheImpresso = isChamdaEncalheImpresso;
	}
	public boolean getIsChamdaEncalheEmail() {
		return isChamdaEncalheEmail;
	}
	public void setIsChamdaEncalheEmail(boolean isChamdaEncalheEmail) {
		this.isChamdaEncalheEmail = isChamdaEncalheEmail;
	}

	

	

}
