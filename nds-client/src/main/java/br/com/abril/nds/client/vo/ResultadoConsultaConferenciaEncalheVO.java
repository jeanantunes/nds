package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;

public class ResultadoConsultaConferenciaEncalheVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private TableModel<CellModelKeyValue<ConferenciaEncalheVO>> tableModelConferenciaEncalhe;
	
	private TableModel<CellModelKeyValue<DebitoCreditoCotaVO>> tableModelDebitoCreditoCota;
	
	private String reparte;
	private String encalhe;
	private String valorVendaDia;
	private String totalDebitoCreditoCota;
	private String valorPagar;

	public String getReparte() {
		return reparte;
	}

	public void setReparte(String reparte) {
		this.reparte = reparte;
	}

	public String getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(String encalhe) {
		this.encalhe = encalhe;
	}

	public String getValorVendaDia() {
		return valorVendaDia;
	}

	public void setValorVendaDia(String valorVendaDia) {
		this.valorVendaDia = valorVendaDia;
	}

	public String getTotalDebitoCreditoCota() {
		return totalDebitoCreditoCota;
	}

	public void setTotalDebitoCreditoCota(String totalDebitoCreditoCota) {
		this.totalDebitoCreditoCota = totalDebitoCreditoCota;
	}

	public String getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(String valorPagar) {
		this.valorPagar = valorPagar;
	}

	public TableModel<CellModelKeyValue<ConferenciaEncalheVO>> getTableModelConferenciaEncalhe() {
		return tableModelConferenciaEncalhe;
	}

	public void setTableModelConferenciaEncalhe(
			TableModel<CellModelKeyValue<ConferenciaEncalheVO>> tableModelConferenciaEncalhe) {
		this.tableModelConferenciaEncalhe = tableModelConferenciaEncalhe;
	}

	public TableModel<CellModelKeyValue<DebitoCreditoCotaVO>> getTableModelDebitoCreditoCota() {
		return tableModelDebitoCreditoCota;
	}

	public void setTableModelDebitoCreditoCota(
			TableModel<CellModelKeyValue<DebitoCreditoCotaVO>> tableModelDebitoCreditoCota) {
		this.tableModelDebitoCreditoCota = tableModelDebitoCreditoCota;
	}
	
	
}
