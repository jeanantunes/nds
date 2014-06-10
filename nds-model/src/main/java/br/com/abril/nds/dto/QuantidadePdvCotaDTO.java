package br.com.abril.nds.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuantidadePdvCotaDTO implements Serializable{
	
	private boolean isCotaPossuiVariosPdvs;

	public boolean isCotaPossuiVariosPdvs() {
		return isCotaPossuiVariosPdvs;
	}

	public void setCotaPossuiVariosPdvs(boolean isCotaPossuiVariosPdvs) {
		this.isCotaPossuiVariosPdvs = isCotaPossuiVariosPdvs;
	}
	
	

}
