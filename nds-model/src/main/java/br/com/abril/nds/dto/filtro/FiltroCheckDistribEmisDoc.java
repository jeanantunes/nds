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

	private boolean isNotaEnvioImpresso;
	private boolean isNotaEnvioEmail;

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
	public boolean getIsNotaEnvioImpresso() {
		return isNotaEnvioImpresso;
	}
	public void setIsNotaEnvioImpresso(boolean isNotaEnvioImpresso) {
		this.isNotaEnvioImpresso = isNotaEnvioImpresso;
	}
	
	public boolean getIsNotaEnvioEmail() {
		return isNotaEnvioEmail;
	}
	public void setIsNotaEnvioEmail(boolean isNotaEnvioEmail) {
		this.isNotaEnvioEmail = isNotaEnvioEmail;
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
