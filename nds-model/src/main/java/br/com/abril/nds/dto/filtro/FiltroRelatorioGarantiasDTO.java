package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;

public class FiltroRelatorioGarantiasDTO extends FiltroDTO implements Serializable{

	private static final long serialVersionUID = -8962205303293463533L;

	private TipoGarantia tipoGarantiaEnum;
	private TipoStatusGarantia statusGarantiaEnum;
	
	private String tipoGarantia;
	private String statusGarantia;
	
	private Date dataBaseCalculo;
	
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
	
	public Date getDataBaseCalculo() {
		return dataBaseCalculo;
	}
	
	public void setDataBaseCalculo(Date dataBaseCalculo) {
		this.dataBaseCalculo = dataBaseCalculo;
	}
	
	/**
	 * @return the tipoGarantiaEnum
	 */
	public TipoGarantia getTipoGarantiaEnum() {
		return tipoGarantiaEnum;
	}
	/**
	 * @param tipoGarantiaEnum the tipoGarantiaEnum to set
	 */
	public void setTipoGarantiaEnum(TipoGarantia tipoGarantiaEnum) {
		this.tipoGarantiaEnum = tipoGarantiaEnum;
	}
	/**
	 * @return the statusGarantiaEnum
	 */
	public TipoStatusGarantia getStatusGarantiaEnum() {
		return statusGarantiaEnum;
	}
	/**
	 * @param statusGarantiaEnum the statusGarantiaEnum to set
	 */
	public void setStatusGarantiaEnum(TipoStatusGarantia statusGarantiaEnum) {
		this.statusGarantiaEnum = statusGarantiaEnum;
	}

}
