package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

public class FiltroRelatorioGarantiasDTO extends FiltroDTO implements Serializable{

	private static final long serialVersionUID = -8962205303293463533L;

	private String tipoGarantia;
	private String statusGarantia;
	
	
	public String getTipoGarantia() {
		return tipoGarantia;
	}
	public void setTipoGarantia(String tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}
	public String getStatusGarantia() {
		return statusGarantia;
	}
	public void setStatusGarantia(String statusGarantia) {
		this.statusGarantia = statusGarantia;
	}
	
	

}
