package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.TipoStatusGarantia;

public class FiltroRelatorioGarantiasDTO extends FiltroDTO implements Serializable{

	private static final long serialVersionUID = -8962205303293463533L;

	private TipoGarantia tipoGarantia;
	private TipoStatusGarantia statusGarantia;
	
	private Date dataBaseCalculo;
	
	public Date getDataBaseCalculo() {
		return dataBaseCalculo;
	}
	
	public void setDataBaseCalculo(Date dataBaseCalculo) {
		this.dataBaseCalculo = dataBaseCalculo;
	}

	public TipoGarantia getTipoGarantia() {
		return tipoGarantia;
	}

	public void setTipoGarantia(TipoGarantia tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
	}

	public TipoStatusGarantia getStatusGarantia() {
		return statusGarantia;
	}

	public void setStatusGarantia(TipoStatusGarantia statusGarantia) {
		this.statusGarantia = statusGarantia;
	}
}