package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroCheckDistribEmisDoc implements Serializable{
	
	
	private boolean isSkipImpresso;
	private boolean isSkipEmail;
	
	private boolean isBoletoImpresso;
	private boolean isBoletoEmail;

	private boolean isBoletoSkipImpresso;
	private boolean isBoletoSkipEmail;

	private boolean isReciboImpresso;
	private boolean isReciboEmail;

	private boolean isNoteEnvioImpresso;
	private boolean isNoteEnvioEmail;

	private boolean isChamdaEncalheImpresso;
	private boolean isChamdaEncalheEmail;
	
	public boolean getIsSkipImpresso() {
		return isSkipImpresso;
	}
	public void setIsSkipImpresso(boolean isSkipImpresso) {
		this.isSkipImpresso = isSkipImpresso;
	}
	public boolean getIsSkipEmail() {
		return isSkipEmail;
	}
	public void setIsSkipEmail(boolean isSkipEmail) {
		this.isSkipEmail = isSkipEmail;
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
	public boolean getIsBoletoSkipImpresso() {
		return isBoletoSkipImpresso;
	}
	public void setIsBoletoSkipImpresso(boolean isBoletoSkipImpresso) {
		this.isBoletoSkipImpresso = isBoletoSkipImpresso;
	}
	public boolean getIsBoletoSkipEmail() {
		return isBoletoSkipEmail;
	}
	public void setIsBoletoSkipEmail(boolean isBoletoSkipEmail) {
		this.isBoletoSkipEmail = isBoletoSkipEmail;
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
